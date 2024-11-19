package org.raindrop.common.enums;

import lombok.Getter;

/**
 * 条件构造器，where条件类型枚举
 */
@Getter
public enum WhereE {
    EQ(1, "等值匹配"),
    NE(2, "非等值匹配"),
    LIKE(3, "模糊查询"),
    LE(4, "小于等于"),
    LT(5, "小于"),
    GE(6, "大于等于"),
    GT(7, "大于"),
    BETWEEN(8, "between范围查询查询"),
    IN(9, "in范围查询"),
    IS_NULL(10, "为空判断"),
    IS_NOT_NULL(11, "非空判断"),
    ORDER_BY_ASC(12, "顺序排序"),
    ORDER_BY_DESC(13, "逆序排序"),
    AND(14, "自定义and"),
    APPLY(15, "自定义查询语句");

    final int key;
    final String msg;

    WhereE(int key, String msg) {
        this.key = key;
        this.msg = msg;
    }
}
