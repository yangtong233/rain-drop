package org.raindrop.common.utils.query.parse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.raindrop.common.enums.WhereE;
import org.raindrop.common.utils.query.BaseWildcardParse;

import java.util.Collection;

/**
 * @Description: TODO
 * @Author: az
 * @Date: 2023-10-29 18:27
 * @Version: 1.0
 */
public class InParse extends BaseWildcardParse {

    @Override
    protected void doParse(QueryWrapper qw, Object fieldValue, String[] fieldNames) {
        if (!(fieldValue instanceof Collection)) {
            return;
        }
        for (String fieldName : fieldNames) {
            qw.in(fieldName, fieldValue);
        }
    }

    @Override
    protected Integer getKey() {
        return WhereE.IN.getKey();
    }
}
