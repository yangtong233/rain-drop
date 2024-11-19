package org.raindrop.common;

/**
 * created by yangtong on 2024/5/14 18:01
 * <p>
 * 封装了通用键值对
 */
public record KeyValuePair<K, V>(K key, V value) {

    /**
     * 反转key和value，并得到一个新的KeyValuePair对象
     *
     * @return 反转了key和value之后的KeyValuePair对象
     */
    public KeyValuePair<V, K> reverse() {
        return new KeyValuePair<>(value, key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != KeyValuePair.class) {
            return false;
        }
        //由于上面已经检查了obj的类型，所以这里可以放心强转
        @SuppressWarnings("unchecked")
        KeyValuePair<K, V> other = (KeyValuePair<K, V>) obj;
        return this.key.equals(other.key) && this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode() * 31 + this.value.hashCode();
    }

    @Override
    public String toString() {
        return "[" + key + " --> " + value + "]";
    }
}
