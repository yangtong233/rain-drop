package org.raindrop.common.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by yangtong on 2024/5/14 18:16
 *
 * @Description: 标识注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Tag {

    String value();

    Class typeHandler();

}
