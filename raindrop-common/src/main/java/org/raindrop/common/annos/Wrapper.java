package org.raindrop.common.annos;

import org.raindrop.common.enums.WhereE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动生成查询器的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.FIELD)
public @interface Wrapper {
    /**
     * 查询条件，默认等值匹配
     */
    WhereE value() default WhereE.EQ;

    /**
     * 查询的字段名，不指定则使用当前修饰字段的下划线形式位字段名
     */
    String[] field() default {};

    /**
     * 应用于所属字段的自定义sql段落
     * @return
     */
    String sql() default "";
}
