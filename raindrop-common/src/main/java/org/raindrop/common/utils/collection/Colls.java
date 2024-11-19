package org.raindrop.common.utils.collection;

import org.raindrop.common.exception.BaseException;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * created by yangtong on 2024/5/25 14:11
 * <br>
 * &#064;Description:  集合工具类
 */
public class Colls {

    public static <T> T getOne(Collection<T> collection, Predicate<T> predicate) {
        List<T> candidateItem = collection.stream().filter(predicate).toList();
        if (candidateItem.size() > 1) {
            throw new BaseException("期待容器集合{}只有一个元素，但是实际发现了{}个", collection, candidateItem.size());
        }
        else if (candidateItem.size() == 1) {
            return candidateItem.getFirst();
        }
        return null;
    }

}
