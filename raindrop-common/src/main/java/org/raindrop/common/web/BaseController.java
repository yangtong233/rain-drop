package org.raindrop.common.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raindrop.common.annos.Marker;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.web.PageUtil;
import org.raindrop.common.utils.excel.Excels;
import org.raindrop.common.utils.query.QueryWrapperGenerator;
import org.raindrop.common.utils.string.Strs;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共控制器类
 * 单表的增删改查接口由于代码高度相似，所以抽取成一个公共Controller
 *
 * @param <T> 实体类
 * @param <S> 实体类对应的service
 */
public abstract class BaseController<T extends PersistenceModel, S extends IService<T>> {

    protected S baseService;

    @Autowired
    public void setBaseService(S baseService) {
        this.baseService = baseService;
    }

    /**
     * 分页查询
     * 子类由于业务的变化，可能分页查询的参数也会变化，此时子类方法会无法覆盖父类方法，
     * 造成子类有两个分页接口，此时应注意子类的分页接口的mapper与父类有所区分
     *
     * @param t 实体类
     */
    @GetMapping("page")
    @Operation(summary = "单表分页查询", description = "单表分页查询")
    @ApiOperationSupport(order = Integer.MAX_VALUE - 5)
    protected R<IPage<T>> page(@ModelAttribute @ParameterObject T t) {
        IPage<T> page = baseService.page(PageUtil.getPage(), QueryWrapperGenerator.getQueryWrapper(t));

        return R.success(page);
    }

    /**
     * 根据id查询对应的实体数据数据
     *
     * @param id 实体id
     * @return 实体数据
     */
    @GetMapping("queryById")
    @Operation(summary = "根据id查询", description = "根据id查询对应的实体数据数据")
    @ApiOperationSupport(order = Integer.MAX_VALUE - 4)
    protected R<T> queryById(String id) {
        return R.success(baseService.getById(id));
    }

    /**
     * 新增或编辑
     * 绝大多数情况下，新增或编辑只是针对一张表的，一般子类无需重写，即使要重写，也要注意接口mapper冲突
     */
    @PostMapping("addOrUpdate")
    @Operation(summary = "新增或编辑", description = "根据有无主键id,新增或编辑数据")
    @ApiOperationSupport(order = Integer.MAX_VALUE - 3)
    protected R<String> addOrUpdate(@RequestBody T t) {
        baseService.saveOrUpdate(t);
        return R.success(t.getId() == null ? "新增成功" : "编辑成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("delete/{ids}")
    @Operation(summary = "删除", description = "根据id删除数据")
    @ApiOperationSupport(order = Integer.MAX_VALUE - 2)
    protected R<String> delete(@Parameter(description = "主键id") @PathVariable String ids) {
        if (Strs.isEmpty(ids)) {
            throw new BaseException("删除接口的ids不能为空");
        }
        List<String> idList = Arrays.asList(ids.split(","));
        baseService.removeBatchByIds(idList);
        return R.success("删除成功");
    }

    @GetMapping("export")
    @Operation(summary = "模板导出", description = "模板导出，请求参数同单表分页查询接口")
    @ApiOperationSupport(order = Integer.MAX_VALUE - 1)
    protected void export(@ModelAttribute @ParameterObject T t, HttpServletResponse resp) throws IOException {
        IPage<T> page = baseService.page(PageUtil.getPage(), QueryWrapperGenerator.getQueryWrapper(t));
        //实体类
        Class<T> entityClass = baseService.getEntityClass();

        //得到实体类name
        String clazzName = null;
        Schema schema = entityClass.getAnnotation(Schema.class);
        if (schema != null) {
            clazzName = Strs.isNotEmpty(schema.name()) ? schema.name() : schema.title();
        }
        if (Strs.isEmpty(clazzName)) {
            Marker marker = entityClass.getAnnotation(Marker.class);
            clazzName = marker != null ?
                    Strs.isNotEmpty(marker.value()) ?
                            marker.value() : marker.name() : null;
        }
        if (Strs.isEmpty(clazzName)) {
            clazzName = entityClass.getSimpleName();
        }

        //设置响应体格式
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", Strs.format("attachment; filename={}.xlsx", clazzName));

        //将数据写入响应体
        Excels.write(page.getRecords(), entityClass, resp.getOutputStream(), clazzName);
    }

    protected <M> M initParams(Class<M> clazz) {
        BufferedReader reader = null;
        Map<String, Object> map = new HashMap<>();
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new BaseException("无法获取请求对象");
            }
            HttpServletRequest request = requestAttributes.getRequest();
            //获取的请求的表单数据
            Map<String, String[]> parameterMap = request.getParameterMap();
            map.putAll(parameterMap);
            //获取请求体的json数据（如果有的话）
            String contentType = request.getHeader("Content-Type");
            if (contentType != null && contentType.contains("application/json")) {
                StringBuilder requestBody = new StringBuilder();
                String line;
                reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                JSONObject body = JSONUtil.parseObj(requestBody.toString());
                map.putAll(body);
                //map.putAll(bodyMap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(reader);
        }

        map.forEach((k, v) -> {
            if (v instanceof String[] singletonArr && singletonArr.length == 1) {
                map.put(k, singletonArr[0]);
            }
        });

        return BeanUtil.copyProperties(map, clazz);
    }


}
