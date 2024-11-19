package org.raindrop.core.db.helper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * created by yangtong on 2024/5/14 16:47
 *
 * @Description: 封装了执行的sql及其参数信息
 */
public class SqlObj {
    /**
     * SQL类型
     */
    private final SqlType sqlType;

    /**
     * 要执行的SQL
     */
    private final String sql;

    /**
     * 参数值
     */
    private List<ParameterObj> parameters;

    public SqlObj(SqlType sqlType, String sql) {
        this.sqlType = sqlType;
        this.sql = sql;
        parameters = new ArrayList<>();
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterObj> getParameters() {
        return parameters;
    }

    public void addParameters(ParameterObj parameter) {
        this.parameters.add(parameter);
    }

    public void parseSql(PreparedStatement ps) {
        parameters.stream()
                .sorted(Comparator.comparing(ParameterObj::getOrder))
                .forEach(p -> p.setSql(ps));
    }

    public static abstract class ParameterObj<T> {
        /**
         * 参数类型
         */
        private Class<T> parameterType;

        /**
         * 参数值
         */
        private T parameterValue;

        /**
         * 该参数对应第几个占位符
         */
        private Integer order;

        /**
         * 通过预处理对象给sql占位符赋值
         *
         * @param ps 预处理对象
         */
        abstract void setSql(PreparedStatement ps);

        public Class<T> getParameterType() {
            return parameterType;
        }

        public void setParameterType(Class<T> parameterType) {
            this.parameterType = parameterType;
        }

        public T getParameterValue() {
            return parameterValue;
        }

        public void setParameterValue(T parameterValue) {
            this.parameterValue = parameterValue;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

    /**
     * sql类型
     */
    public enum SqlType {
        INSERT, DELETE, UPDATE, SELECT
    }
}
