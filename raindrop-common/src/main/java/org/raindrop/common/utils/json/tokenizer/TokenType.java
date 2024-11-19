package org.raindrop.common.utils.json.tokenizer;

/**
 * created by yangtong on 2024/5/18 22:40
 *
 * @Description: JSON字符串的token类型枚举
 */
public enum TokenType {
    /**
     * 对应JSON中的'{'，表示一个对象的起始
     */
    BEGIN_OBJECT(1),
    /**
     * 对应JSON中的'}'，表示一个对象的结束
     */
    END_OBJECT(2),
    /**
     * 对应JSON中的'['，表示一个数组的起始
     */
    BEGIN_ARRAY(4),
    /**
     * 对应JSON中的']'，表示一个数组的结束
     */
    END_ARRAY(8),
    /**
     * 表示JSON中的NULL类型
     */
    NULL(16),
    /**
     * 表示JSON中的数值类型
     */
    NUMBER(32),
    /**
     * 表示JSON中的字符串类型
     */
    STRING(64),
    /**
     * 表示JSON中的BOOLEAN类型
     */
    BOOLEAN(128),
    /**
     * JSON中的':'，表示键和值的连接符号
     */
    SEP_COLON(256),
    /**
     * JSON中的','，字段的分隔符
     */
    SEP_COMMA(512),
    /**
     * 表示JSON文档结束
     */
    END_DOCUMENT(1024);

    /**
     * 每种TOKEN类型对应一个编号
     */
    private int code;

    TokenType(int code) {
        this.code = code;
    }

    public int getTokenCode() {
        return code;
    }
}
