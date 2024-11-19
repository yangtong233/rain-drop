package org.raindrop.common.enums;

/**
 * created by yangtong on 2024/6/5 下午5:11
 *
 * @Description: 占位符的前后缀枚举
 */
public enum Placeholder {
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    LEFT_CURLY_BRACE("{"),
    LEFT_CURLY_BRACE_PLUS("#{"),
    LEFT_CURLY_BRACE_DOLLAR("${"),
    RIGHT_CURLY_BRACE("}"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    ;

    public final String placeholder;
    Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
