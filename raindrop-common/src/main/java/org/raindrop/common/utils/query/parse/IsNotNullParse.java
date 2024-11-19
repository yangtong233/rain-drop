package org.raindrop.common.utils.query.parse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.enums.WhereE;
import org.raindrop.common.utils.query.BaseWildcardParse;

/**
 * @Description: TODO
 * @Author: az
 * @Date: 2023-10-29 18:29
 * @Version: 1.0
 */
public class IsNotNullParse extends BaseWildcardParse {

    @Override
    protected void doParse(QueryWrapper qw, Object fieldValue, String[] fieldNames) {
        for (String fieldName : fieldNames) {
            qw.isNotNull(fieldName);
        }
    }

    @Override
    protected Integer getKey() {
        return WhereE.IS_NOT_NULL.getKey();
    }
}
