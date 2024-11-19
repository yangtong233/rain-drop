package org.raindrop.core.web.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.web.PageUtil;
import org.raindrop.core.web.tool.mapper.SysDictMapper;
import org.raindrop.core.web.tool.model.po.SysDict;
import org.raindrop.core.web.tool.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典操作
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public IPage<SysDict> listDict(String dictName, String dictCode) {
        return this.page(PageUtil.getPage(), Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getType, 1)
                .like(StrUtil.isNotEmpty(dictName), SysDict::getDictName, dictName)
                .like(StrUtil.isNotEmpty(dictCode), SysDict::getDictCode, dictCode)
                .orderByAsc(SysDict::getSort));
    }

    @Override
    public List<SysDict> listDictItemByPid(String id) {
        return this.list(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getType, 0)
                .eq(SysDict::getPid, id)
                .orderByAsc(SysDict::getSort));
    }

    @Override
    public Map<String, String> getDict(String code) {
        SysDict dict = getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictCode, code));
        if (dict == null) {
            throw new BaseException("没有code为{}的字典", code);
        }
        List<SysDict> dictItems = list(
                Wrappers.<SysDict>lambdaQuery()
                        .eq(SysDict::getPid, dict.getId())
        );

        return dictItems.stream().collect(Collectors.toMap(SysDict::getItemValue, SysDict::getItemText));
    }


    @Override
    public void deleteDict(String code) {
        SysDict dict = getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictCode, code));
        if (dict != null) {
            //删除字典
            remove(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getId, dict.getId()));
            //删除该字典的字典项
            remove(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getPid, dict.getId()));
        }
    }

    @Override
    public void deleteDictItem(String code, String itemValue) {
        SysDict dict = getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictCode, code));
        if (dict != null) {
            remove(
                    Wrappers.<SysDict>lambdaQuery()
                            .eq(SysDict::getPid, dict.getId())
                            .eq(SysDict::getItemValue, itemValue)
            );
        }
    }

    @Override
    public String getItemText(String code, String itemValue) {
        //得到字典
        SysDict dict = this.getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictCode, code));
        //根据字典id和字典项code，得到字典项
        SysDict dictItem = this.getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getPid, dict.getId()).eq(SysDict::getDictCode, itemValue));

        return dictItem == null ? "" : dictItem.getDictName();
    }


}
