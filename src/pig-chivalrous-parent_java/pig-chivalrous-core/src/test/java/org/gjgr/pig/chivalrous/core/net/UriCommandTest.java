package org.gjgr.pig.chivalrous.core.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @Author gwd
 * @Time 12-08-2018 Saturday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More: [scheme:][//authority][path][?query][#fragment]
 */
public class UriCommandTest {

    @Test
    public void test() {
        String url = "http://demo.ab.c.com/a/b?a=2&d我解放军=333#aaa";
        try {
            URI uri = new URI(url);
            String string = uri.getRawQuery();
            String[] strings = string.split("&");
            Map<String, String> stringMap = new HashMap<>();
            if (strings.length > 0) {
                for (String s : strings) {
                    String[] ss = s.split("=");
                    if (ss.length == 2) {
                        stringMap.put(ss[0], ss[1]);
                    }
                }
            }
        } catch (URISyntaxException e) {
        }
    }
}
