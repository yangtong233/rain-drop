package org.raindrop.core.web.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.tool.model.po.SysDict;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
}
