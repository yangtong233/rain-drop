package org.raindrop.core.db.helper;

import java.sql.ResultSet;
import java.util.Map;

/**
 * created by yangtong on 2024/5/14 16:30
 *
 * @Description: 处理查询结果集的回调
 */
@FunctionalInterface
public interface ResultHandler {
    //将查询结果集映射成Map
    Map<String, QueryResultField> mapping(ResultSet rs);
}
