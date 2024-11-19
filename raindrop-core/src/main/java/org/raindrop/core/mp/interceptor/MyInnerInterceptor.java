package org.raindrop.core.mp.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MyInnerInterceptor implements InnerInterceptor {
    /**
     * 在执行查询之前，判断是否需要执行查询操作。默认返回 true，表示将执行查询操作
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     * @return
     * @throws SQLException
     */
    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        return InnerInterceptor.super.willDoQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    /**
     * 在执行查询之前的拦截方法。你可以在这里进行一些处理，比如修改查询参数、动态构建 SQL 等等
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     * @throws SQLException
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        InnerInterceptor.super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    /**
     * 在执行更新操作之前，判断是否需要执行更新操作。默认返回 true，表示将执行更新操作
     * @param executor
     * @param ms
     * @param parameter
     * @return
     * @throws SQLException
     */
    @Override
    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        return InnerInterceptor.super.willDoUpdate(executor, ms, parameter);
    }

    /**
     * 在执行更新操作之前的拦截方法。你可以在这里进行一些处理，比如修改更新参数、动态构建 SQL 等等。
     * @param executor
     * @param ms
     * @param parameter
     * @throws SQLException
     */
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        InnerInterceptor.super.beforeUpdate(executor, ms, parameter);
    }

    /**
     * 在准备 Statement 之前的拦截方法。你可以在这里进行一些处理，比如设置 Statement 的超时时间等
     * @param sh
     * @param connection
     * @param transactionTimeout
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        InnerInterceptor.super.beforePrepare(sh, connection, transactionTimeout);
    }

    /**
     * 在获取 BoundSql 对象之前的拦截方法。你可以在这里进行一些处理，比如修改 BoundSql 对象中的参数等
     * @param sh
     */
    @Override
    public void beforeGetBoundSql(StatementHandler sh) {
        InnerInterceptor.super.beforeGetBoundSql(sh);
    }

    /**
     * 设置插件属性的方法。你可以在这里接收配置的属性值，并进行一些初始化操作。
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        InnerInterceptor.super.setProperties(properties);
    }
}
