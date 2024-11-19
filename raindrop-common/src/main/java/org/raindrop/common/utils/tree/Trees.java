package org.raindrop.common.utils.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树状数据工具类
 */
public class Trees {

    /**
     * 将目标数据变成具有层级的数组结构
     * @param items 目标数据
     * @param <T> 目标数据元素类型
     * @return 树状数据
     */
    public static <T extends BaseTree<T>> List<T> toTree(List<T> items) {
        //将list映射成map
        Map<String, T> nodeMap = items.stream().collect(Collectors.toMap(BaseTree::getId, Function.identity()));
        //存储所有顶层节点
        List<T> roots = new ArrayList<>();

        //给每一个元素找父亲
        for (T item : items) {
            if ("0".equals(item.getPid())) {
                roots.add(item);
            }
            else {
                T parent = nodeMap.get(item.getPid());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        List<T> children = new ArrayList<>();
                        children.add(item);
                        parent.setChildren(children);
                    }
                    else {
                        parent.getChildren().add(item);
                    }
                }
            }
        }

        return roots;
    }
}
