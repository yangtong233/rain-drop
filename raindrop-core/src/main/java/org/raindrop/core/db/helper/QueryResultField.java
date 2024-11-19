package org.raindrop.core.db.helper;

/**
 * created by yangtong on 2024/5/14 16:36
 *
 * @Description: 封装查询字段相关信息
 */
public class QueryResultField {

    /**
     * 数据库字段名称
     */
    private String fieldName;

    /**
     * 数据库字段的类型
     */
    private String fieldType;

    /**
     * 数据库字段对应的Java类型
     */
    private Class javaType;

    /**
     * 字段真实值
     */
    private Object fieldValue;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
