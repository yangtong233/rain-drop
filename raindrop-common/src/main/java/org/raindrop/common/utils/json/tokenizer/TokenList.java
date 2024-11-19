package org.raindrop.common.utils.json.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yangtong on 2024/5/18 23:05
 *
 * @Description: 存储词法解析所得的token流
 */
public class TokenList {
    private List<Token> tokens = new ArrayList<Token>();

    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }

}
