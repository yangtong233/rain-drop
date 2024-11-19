package org.raindrop.core.web.tool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.raindrop.common.web.BaseController;
import org.raindrop.common.web.R;
import org.raindrop.core.web.tool.model.po.SysDict;
import org.raindrop.core.web.tool.service.SysDictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典接口
 */
@RestController
@RequestMapping("/sys/dict")
@AllArgsConstructor
@Tag(name = "数据字典")
public class SysDictController extends BaseController<SysDict, SysDictService> {

    @GetMapping("/listDict")
    @Operation(summary = "分页查询字典")
    public R<IPage<SysDict>> list(String dictName, String dictCode) {
        return R.success(baseService.listDict(dictName, dictCode));
    }

    @GetMapping("/listDictItemByPid")
    @Operation(summary = "根据父字典id查询的所有字典项")
    public R<List<SysDict>> listDictItemByPid(String pid) {
        return R.success(baseService.listDictItemByPid(pid));
    }

    @GetMapping("/listDictItemByCode")
    @Operation(summary = "根据字典code查询的所有字典项")
    public R<Map<String, String>> listDictItemByCode(String code) {
        return R.success(baseService.getDict(code));
    }

    @GetMapping("/getItemText")
    @Operation(summary = "得到字典项的文本内容", description = "根据字典code和字典项value得到字典项的text")
    public R<String> getItemText(String code, String itemValue) {
        String dictName = baseService.getItemText(code, itemValue);
        return R.success(dictName);
    }

    @DeleteMapping("deleteDict/{code}")
    @Operation(summary = "删除字典", description = "根据code删除字典")
    public R<String> deleteDict(@PathVariable String code) {
        baseService.deleteDict(code);
        return R.success("删除成功");
    }

    @DeleteMapping("deleteDictItem/{code}/{itemValue}")
    @Operation(summary = "删除字典项", description = "根据字典code和字典项value删除字典项")
    public R<String> deleteDictItem(@PathVariable String code, @PathVariable String itemValue) {
        baseService.deleteDictItem(code, itemValue);
        return R.success("删除成功");
    }

}
