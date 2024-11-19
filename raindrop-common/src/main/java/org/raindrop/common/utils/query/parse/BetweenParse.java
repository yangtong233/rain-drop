package org.raindrop.common.utils.query.parse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.enums.WhereE;
import org.raindrop.common.utils.query.BaseWildcardParse;

import java.util.Collection;

/**
 * @Description: TODO
 * @Author: az
 * @Date: 2023-10-29 18:07
 * @Version: 1.0
 */
public class BetweenParse extends BaseWildcardParse {

    @Override
    protected void doParse(QueryWrapper qw, Object fieldValue, String[] fieldNames) {
        if (!(fieldValue instanceof Collection)) {
            return;
        }
        Object[] values = null;
        values = ((Collection<?>) fieldValue).toArray(values);
        for (String fieldName : fieldNames) {
            qw.between(fieldName, values[0], values[1]);
        }
    }

    @Override
    protected Integer getKey() {
        return WhereE.BETWEEN.getKey();
    }

}
