package org.raindrop.core.web.sys.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.raindrop.common.enums.Resp;
import org.raindrop.common.web.R;
import org.raindrop.core.auth.AuthProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by yangtong on 2024/5/25 16:04
 *
 * @Description: 系统公共参数接口
 */
@RestController
@RequestMapping("/sys")
@Tag(name = "系统公共参数接口")
public class SysPublicController {

    @Autowired
    private AuthProperty authProperty;

    @GetMapping("/listResCode")
    @Operation(summary = "查询系统接口响应状态码")
    public R<List<JSONObject>> listSysStatusCode() {
        List<JSONObject> list = Arrays.stream(Resp.values()).sorted(Comparator.comparing(c -> c.code)).map(code -> {
            JSONObject json = new JSONObject();
            json.putOnce("code", code.code);
            json.putOnce("status", code.status);
            json.putOnce("message", code.message);
            return json;
        }).collect(Collectors.toList());
        return R.success(list);
    }

    @GetMapping("/getTokenTimeOut")
    @Operation(summary = "获取系统token最久不操作过期时间")
    public R<Map<String, Long>> getTokenTimeOut() {
        return R.success(Map.of("tokenTimeOut", authProperty.getTimeOut()));
    }

}
