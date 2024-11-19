package org.raindrop.common.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel导出时附带的参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.FIELD)
public @interface Excel {
    /**
     * 对应模板的占位符，默认修饰的字段名
     */
    String value() default "";

    /**
     * 该列标题名称，针对没有模板的情况
     */
    String name() default "";

}
