package org.raindrop.common.utils.json;

import org.raindrop.common.utils.json.model.JsonObject;
import org.raindrop.common.utils.json.parser.Parser;
import org.raindrop.common.utils.string.CharReader;
import org.raindrop.common.utils.json.tokenizer.TokenList;
import org.raindrop.common.utils.json.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * created by yangtong on 2024/5/18 23:31
 *
 * @Description: JSON工具类
 */
public class Jsons {
    private static Tokenizer tokenizer = new Tokenizer();

    private static Parser parser = new Parser();

    public static JsonObject toJsonObject(String json) throws IOException {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return (JsonObject)parser.parse(tokens);
    }
}
