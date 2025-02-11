package org.raindrop.core.web.rabc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.raindrop.common.web.BaseController;
import org.raindrop.core.web.rabc.model.po.SysDepart;
import org.raindrop.core.web.rabc.service.SysDepartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by yangtong on 2025/2/11 18:05:22
 */
@RestController
@RequestMapping("/rabc/depart")
@AllArgsConstructor
@Tag(name = "部门")
public class SysDepartController extends BaseController<SysDepart, SysDepartService> {
}
