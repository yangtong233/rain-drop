package org.raindrop.common.utils.query;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.annos.Wrapper;

/**
 * 通配符解析抽象类
 */
public abstract class BaseWildcardParse {

    /**
     * 所有解析类的公共操作
     * @param fieldDescription 字段描述
     * @param qw 条件构造器
     */
    public void parse(FieldDescription fieldDescription, QueryWrapper qw) {
        Wrapper wrapper = fieldDescription.wrapper;
        if (StrUtil.isNotEmpty(wrapper.sql())) {
            qw.apply(StrUtil.format("{} {}", fieldDescription.fieldName, wrapper.sql()));
            return;
        }
        if (wrapper.field() == null || wrapper.field().length == 0) {
            if (ObjectUtil.isNotEmpty(fieldDescription.fieldValue)) {
                doParse(qw, fieldDescription.fieldValue, StrUtil.toUnderlineCase(fieldDescription.fieldName));
            }
        }
        else {
            doParse(qw, fieldDescription.fieldValue, wrapper.field());
        }
    }

    /**
     * 不同解析的私有操作
     */
    protected abstract void doParse(QueryWrapper qw, Object fieldValue, String... fieldNames);

    /**
     * 获取该解析器在WhereType中的key
     * @return
     */
    protected abstract Integer getKey();

}
