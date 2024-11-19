package org.raindrop.common.utils.bean;

import org.raindrop.common.KeyValuePair;
import org.raindrop.common.annos.Marker;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.string.Strs;
import org.springframework.lang.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Beans {

    /**
     * 列举所有基本类型
     */
    private static final Set<Class<?>> BASIC_TYPES = new HashSet<>(
            List.of(
                    Boolean.class, Byte.class, Character.class, Double.class,
                    Float.class, Integer.class, Long.class, Short.class,
                    String.class, Void.class
            ));

    /**
     * 判断指定类型是单一类型还是复合类型
     *
     * @param clazz 类型
     * @return
     */
    public static Boolean isSingletonType(Class<?> clazz) {
        return clazz.isPrimitive() || BASIC_TYPES.contains(clazz);
    }

    /**
     * 解构对象
     *
     * @param pair 键值对，键->对象名称，值->对象，结构的就是这个值
     */
    public static Map<String, Object> deconstruct(KeyValuePair<String, Object> pair, Map<String, Object> map) {
        if (isSingletonType(pair.value().getClass())) {
            map.put(pair.key(), pair.value());
            return map;
        }
        Field[] fields = pair.value().getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                //字段值
                Object fieldValue = field.get(pair.value());
                if (fieldValue == null) {
                    continue;
                }
                //字段名称
                String fieldName = field.getName();
                //获取字段类型
                Class<?> type = field.getType();
                //是基本类型
                if (isSingletonType(type)) {
                    map.put(fieldName, field.get(pair.value()));
                }
                //非基本类型，则递归解构
                else {
                    deconstruct(new KeyValuePair<>(fieldName, fieldValue), map);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 得到clazz类型的继承层级，链表前面的元素层级低，后面的元素层级高
     *
     * @param clazz 被解析的类型
     * @return 得到clazz类型的继承层级
     */
    public static List<Class<?>> extendsHierarchy(Class<?> clazz) {
        List<Class<?>> list = new LinkedList<>();
        while (clazz != null) {
            list.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    /**
     * 将对象obj的属性解析成map集合，并忽略空属性
     *
     * @param obj 对象
     * @param <T> 对象类型
     * @return map集合
     */
    public static <T> Map<String, Object> toMap(@NonNull T obj) {
        return toMap(obj, true);
    }

    /**
     * 将对象obj的属性解析成map集合
     *
     * @param obj         对象
     * @param ignoreEmpty 是否忽略空字段
     * @param <T>         对象类型
     * @return map集合
     */
    public static <T> Map<String, Object> toMap(@NonNull T obj, boolean ignoreEmpty) {
        Map<String, Object> map = new HashMap<>(16);
        //忽略空字段
        if (ignoreEmpty) {
            parseBeanField(obj, (key, value) -> {
                if (Strs.isNotEmpty(key)) {
                    map.put(key, value);
                }
            });
        }
        //不忽略空字段
        else {
            parseBeanField(obj, map::put);
        }

        return map;
    }

    /**
     * 解析对象obj的属性
     *
     * @param obj      对象
     * @param consumer map集合的自定义添加逻辑
     * @param <T>      对象类型
     */
    public static <T> void parseBeanField(@NonNull T obj, @NonNull BiConsumer<String, Object> consumer) {
        //1.反射得到所有属性
        Field[] candidateFields = obj.getClass().getDeclaredFields();

        try {
            //2.遍历属性
            for (Field candidateField : candidateFields) {
                //私有属性可访问
                candidateField.setAccessible(true);
                Marker marker = candidateField.getAnnotation(Marker.class);
                //如果没有marker标记，则使用字段名称作为key，否则使用注解作为key
                //String key = marker != null ? Strs.isNotEmpty(marker.value()) ? marker.value() : marker.name() : candidateField.getName();
                String key = marker != null && Strs.isNotEmpty(marker.value()) ?
                        marker.value() : candidateField.getName();
                //3.往map添加数据
                consumer.accept(key, candidateField.get(obj));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 属性拷贝，根据targetClass类型构造新对象，然后将source对象的属性拷贝到target对象的对应的属性上
     * 目标类型必须具有无参构造器
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param <T>         目标类型
     * @return 被属性填充的目标对象
     */
    public static <T> T copy(@NonNull Object source, @NonNull Class<T> targetClass) {
        try {
            //得到无参构造器
            Constructor<T> constructor = targetClass.getConstructor();
            T target = constructor.newInstance();
            return copy(source, target);
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    /**
     * 属性拷贝，将source对象的属性拷贝到target对象的对应的属性上
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T>    目标对象类型
     * @return 被属性填充后的目标对象
     */
    public static <T> T copy(@NonNull Object source, @NonNull T target) {
        //源对象的字段缓存
        Map<String, Field> sourceFieldMap = Arrays.stream(source.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, field -> field));
        //目标对象的字段数组
        Field[] targetCandidateField = target.getClass().getDeclaredFields();

        //遍历目标字段，从源对象字段中找到对应的字段赋值
        try {
            for (Field targetField : targetCandidateField) {
                targetField.setAccessible(true);
                Field sourceField = sourceFieldMap.get(targetField.getName());
                if (sourceField != null && targetField.getType().isAssignableFrom(sourceField.getType())) {
                    targetField.set(target, sourceField.get(source));
                }
            }
            return target;
        } catch (IllegalAccessException e) {
            throw new BaseException(e);
        }
    }

}
