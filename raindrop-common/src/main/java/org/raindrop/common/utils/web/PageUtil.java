package org.raindrop.common.utils.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.string.Strs;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页相关工具类
 */
public class PageUtil {

    /**
     * 有时候，分页查询时，分页泛型是数据库实体类型，但是查询出数据后进行返回时，需要将分页的泛型转为vo类型，
     * 就需要进行转换操作，该方法封装了转换操作的代码
     *
     * @param oldPage 原始分页对象
     * @param newList 新分页对象的数据集合
     * @param <T>     新分页对象的泛型
     * @return 转换后的新分页对象
     */
    public static <T> IPage<T> transPage(IPage<?> oldPage, List<T> newList) {
        IPage<T> page = new Page<>();
        BeanUtil.copyProperties(oldPage, page);
        page.setRecords(newList);
        return page;
    }

    /**
     * 对list进行分页操作
     *
     * @param pageNo 当前页
     * @param pageSize 每页尺寸
     * @param list 原始集合
     * @return 分页后的数据
     * @param <T> 数据类型
     */
    public static <T> IPage<T> toPage(Long pageNo, Long pageSize, List<T> list) {
        return toPage(new Page<>(pageNo, pageSize), list);
    }

    /**
     * 对list进行分页操作
     * @param page 分页对象
     * @param list 原始集合
     * @return 分页后的数据
     * @param <T> 数据类型
     */
    public static <T> IPage<T> toPage(IPage<T> page, List<T> list) {
        long pageNo = page.getCurrent();
        long pageSize = page.getSize();

        //计算偏移量
        long startIndex = (pageNo - 1) * pageSize;
        if (list.size() <= pageSize) {
            page.setPages(1).setSize(pageSize)
                    .setCurrent(1).setTotal(list.size()).setRecords(list);
        }
        //计算分页数据
        long pages;
        if (list.size() % pageSize == 0) {
            pages = list.size() / pageSize;
        } else {
            pages = list.size() / pageSize + 1;
        }
        if (pageNo * pageSize > list.size()) {
            pageNo = pages;
        }

        List<T> records = new ArrayList<>();
        for (long i = startIndex; i < startIndex + pageSize && i < list.size(); i++) {
            records.add(list.get((int) i));
        }
        //组装分页数据
        page.setPages(pages)
                .setSize(pageSize)
                .setCurrent(pageNo)
                .setTotal(list.size())
                .setRecords(records);
        return page;
    }

    /**
     * 对于分页查询接口，pageNo和pageSize这两个参数是必须的，每次都定义这两个参数并传递封装这两个成一个Page对象略显繁琐
     * 该方法可以直接从请求对象中获取这两个参数并封装成一个Page对象返回，可以在程序的任何地方调用，故而省去了参数传递
     *
     * @return 分页对象
     */
    public static <T> IPage<T> getPage() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BaseException("无法获取请求对象");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        //得到当前页
        String current = request.getParameter("current");
        if (Strs.isEmpty(current)) {
            current = request.getParameter("pageNo");
        }
        //得到每页尺寸
        String size = request.getParameter("size");
        if (Strs.isEmpty(size)) {
            size = request.getParameter("pageSize");
        }
        long pageNo = Convert.convert(Long.class, current, 1L);
        long pageSize = Convert.convert(Long.class, size, 10L);
        return new Page<>(pageNo, pageSize);
    }

}