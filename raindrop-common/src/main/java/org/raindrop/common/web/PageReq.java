package org.raindrop.common.web;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页请求的基础参数
 */
@Schema(name = "基础参数对象")
public abstract class PageReq {
    /**
     * 当前页
     */
    @Schema(name = "当前页")
    private Long current;
    /**
     * 每页大小
     */
    @Schema(name = "每页大小")
    private Long size;
}
