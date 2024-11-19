package org.raindrop.common.utils.json;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Test;
import org.raindrop.common.utils.json.model.JsonObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * created by yangtong on 2024/5/18 23:22
 *
 * @Description: JSON测试类
 */
public class JsonTest {

    @Test
    public void testJsonStr() throws IOException {
        String json = "{\"name\":\"张三\"}";
        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject) jsonParser.fromJSON(json);
        System.out.println(jsonObject);
    }

    @Test
    public void testJsonFile() throws Exception {
        String json = ResourceUtil.readStr("music.json", StandardCharsets.UTF_8);

        JSONParser jsonParser = new JSONParser();
        JsonObject jsonObject = (JsonObject) jsonParser.fromJSON(json);
        System.out.println(jsonObject);

    }
}
