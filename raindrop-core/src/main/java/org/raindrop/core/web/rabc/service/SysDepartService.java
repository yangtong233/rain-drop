package org.raindrop.core.web.rabc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.raindrop.core.web.rabc.model.po.SysDepart;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by yangtong on 2025/2/11 18:00:02
 */
@Transactional(rollbackFor = Exception.class)
public interface SysDepartService extends IService<SysDepart> {
}
