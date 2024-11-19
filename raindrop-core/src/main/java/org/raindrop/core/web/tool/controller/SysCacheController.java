package org.raindrop.core.web.tool.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raindrop.common.web.R;
import org.raindrop.core.cache.C;
import org.raindrop.core.cache.CacheObjResp;
import org.raindrop.core.web.tool.model.req.AddOrEditCacheReq;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * created by yangtong on 2024/5/31 15:12
 *
 * @Description: 缓存操作控制器
 */
@RestController
@RequestMapping("/tool/cache")
@AllArgsConstructor
@Tag(name = "缓存")
public class SysCacheController {


    @GetMapping("/listCache")
    @Operation(summary = "查询指定条件的所有缓存")
    public R<List<CacheObjResp>> list(String key) {
        return R.success(C.listAll(key));
    }

    @PostMapping("/addCache")
    @Operation(summary = "添加缓存")
    public R<String> addCache(@Valid @RequestBody AddOrEditCacheReq req) {
        if (req.getTimeout() != null) {
            C.set(req.getKey(), req.getValue(), req.getTimeout(), TimeUnit.SECONDS);
        } else {
            C.set(req.getKey(), req.getValue());
        }
        return R.success("添加成功");
    }

    @PutMapping("/editCache")
    @Operation(summary = "编辑缓存", description = "编辑缓存")
    public R<String> editCache(@Valid @RequestBody AddOrEditCacheReq req) {
        C.delete(req.getKey());
        addCache(req);
        return R.success("编辑成功");
    }

    @DeleteMapping("/deleteCache/{key}")
    @Operation(summary = "删除指定key的缓存")
    public R<String> deleteCache(@PathVariable String key) {
        C.delete(key);
        return R.success("删除成功");
    }

}
