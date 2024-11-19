package org.raindrop.common.utils.query.parse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.enums.WhereE;
import org.raindrop.common.utils.query.BaseWildcardParse;

/**
 * @Description: TODO
 * @Author: az
 * @Date: 2023-10-29 18:07
 * @Version: 1.0
 */
public class GeParse extends BaseWildcardParse {

    @Override
    protected void doParse(QueryWrapper qw, Object fieldValue, String[] fieldNames) {
        for (String fieldName : fieldNames) {
            qw.ge(fieldName, fieldValue);
        }
    }

    @Override
    protected Integer getKey() {
        return WhereE.GE.getKey();
    }
}
