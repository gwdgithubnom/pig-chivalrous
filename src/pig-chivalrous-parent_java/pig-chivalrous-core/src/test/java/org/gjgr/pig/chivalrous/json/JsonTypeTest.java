package org.gjgr.pig.chivalrous.json;

import com.google.gson.JsonObject;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 10-29-2018  Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class JsonTypeTest {
    @Test
    public void testJsonObject() {
        String str = "{}";
        com.google.gson.JsonObject json = JsonCommand.fromJson(str, JsonObject.class);
        System.out.println(json.toString());
    }
}
