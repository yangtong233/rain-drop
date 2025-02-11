package org.raindrop.core.dynamicsql;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

public class WkDynamicSqlSource implements SqlSource {

    private final Configuration configuration;
    private final SqlNode rootSqlNode;

    public WkDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        // 执行完上面的方法，在这里可以直接获取到解析后带#{} ${}的sql
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        // 执行完上面的方法，就会#{} ${} 给替换成jdbc的问号
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        context.getBindings().forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }

    public String getWkParseSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        // 在这里可以直接获取到解析后带#{} ${}的sql
        return context.getSql();
//        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
//        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
//        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
//        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//        context.getBindings().forEach(boundSql::setAdditionalParameter);
//        return boundSql;
    }

}
