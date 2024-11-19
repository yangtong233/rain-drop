package org.raindrop.common.utils.query;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.annos.Wrapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * mybatis-plus专用
 * 根据实体类得到QueryWrapper对象
 */
@Slf4j
public class QueryWrapperGenerator {

    public final static Map<Integer, BaseWildcardParse> map;

    static {
        map = new HashMap<>();
        try {
            Set<Class<?>> classes = ClassUtil.scanPackageBySuper(BaseWildcardParse.class.getPackage().getName() + ".parse", BaseWildcardParse.class);
            for (Class<?> clazz : classes) {
                BaseWildcardParse parse = (BaseWildcardParse)clazz.newInstance();
                map.put(parse.getKey(), parse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据实体类字段的注解，生成条件构造器
     * @param <T> 实体类
     * @return 条件构造器
     */
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        Field[] fields = ReflectUtil.getFields(entity.getClass(), field -> field.isAnnotationPresent(Wrapper.class));
        QueryWrapper<T> qw = new QueryWrapper<>();

        Arrays.stream(fields).map(field -> {
            field.setAccessible(true);
            FieldDescription description = new FieldDescription();
            try {
                description.wrapper = field.getAnnotation(Wrapper.class);
                description.fieldType = field.getType();
                description.fieldName = field.getName();
                description.fieldValue = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return description;
        }).forEach(description ->{
            int key = description.wrapper.value().getKey();
            BaseWildcardParse parse = map.get(key);
            parse.parse(description, qw);
        });

        return qw;
    }

}
