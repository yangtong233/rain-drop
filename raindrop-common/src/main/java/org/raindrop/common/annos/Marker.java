package org.raindrop.common.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by yangtong on 2024/6/4 22:13:34
 * 打标记，通用注解，至于该注解有什么用，就得看对应的解析程序是怎么写的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Marker {

    /**
     * 标记值
     * @return
     */
    String value() default "";
    /**
     * 标记名称
     */
    String name() default "";

    String dictCode() default "";

    /**
     * 宽度，专用于生成excel表格
     */
    int width() default 15;
}
