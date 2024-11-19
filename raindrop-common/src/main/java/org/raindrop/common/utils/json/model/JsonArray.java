package org.raindrop.common.utils.json.model;

import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.json.BeautifyJsonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * created by yangtong on 2024/5/18 23:18
 *
 * @Description:
 */
public class JsonArray {

    private List list = new ArrayList();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public JsonObject getJsonObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new BaseException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    public JsonArray getJsonArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new BaseException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }

    public Iterator iterator() {
        return list.iterator();
    }

}
