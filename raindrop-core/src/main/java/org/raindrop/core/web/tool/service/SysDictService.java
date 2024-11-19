package org.raindrop.core.web.tool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.raindrop.core.web.tool.model.po.SysDict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
public interface SysDictService extends IService<SysDict> {
    /**
     * 查询所有字典
     */
    IPage<SysDict> listDict(String dictName, String dictCode);

    /**
     * 根据字典id查询该字典的字典项
     */
    List<SysDict> listDictItemByPid(String id);

    /**
     * 删除字典
     */
    void deleteDict(String code);

    /**
     * 根据字典code和字典项code得到字典项的中文名称
     * @param code 字典code，比如sex
     * @param itemValue 字典项code，比如1
     * @return 字典项名称，比如根据sex和1，就能得到"男"
     */
    String getItemText(String code, String itemValue);

    /**
     * 根据字典code查询的所有字典项
     * @param code 字典编码
     * @return 字典项的值value与text的映射
     */
    Map<String, String> getDict(String code);

    /**
     * 根据字典code和字典项value删除字典项
     * @param code 字典的code
     * @param itemValue 字典项的value
     */
    void deleteDictItem(String code, String itemValue);
}
