package org.raindrop.common.utils.json.tokenizer;

import lombok.Data;

/**
 * created by yangtong on 2024/5/18 22:46
 *
 * @Description: 存储具体的TOKEN类型及其字面量
 */
@Data
public class Token {

    /**
     * TOKEN类型
     */
    private TokenType tokenType;
    /**
     * 字面量
     */
    private String value;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

}
