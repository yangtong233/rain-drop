package org.raindrop.common.utils.query.parse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.enums.WhereE;
import org.raindrop.common.utils.query.BaseWildcardParse;

/**
 * like解析
 */
public class LikeParse extends BaseWildcardParse {

    @Override
    protected void doParse(QueryWrapper qw, Object fieldValue, String[] fieldNames) {
        for (String fieldName : fieldNames) {
            qw.like(fieldName, fieldValue);
        }
    }

    @Override
    protected Integer getKey() {
        return WhereE.LIKE.getKey();
    }
}
