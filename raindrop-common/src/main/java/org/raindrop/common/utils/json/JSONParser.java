package org.raindrop.common.utils.json;

import org.raindrop.common.utils.json.parser.Parser;
import org.raindrop.common.utils.string.CharReader;
import org.raindrop.common.utils.json.tokenizer.TokenList;
import org.raindrop.common.utils.json.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * created by yangtong on 2024/5/18 23:20
 *
 * @Description:
 */
public class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();

    private Parser parser = new Parser();

    public Object fromJSON(String json) throws IOException {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }

}
