package org.gjgr.pig.chivalrous.web.http.demo;

import java.util.HashMap;

import org.gjgr.pig.chivalrous.core.lang.Console;
import org.gjgr.pig.chivalrous.web.http.Header;
import org.gjgr.pig.chivalrous.web.http.HttpRequest;
import org.gjgr.pig.chivalrous.web.http.HttpUtil;

/**
 * Post请求样例
 *
 * @author Looly
 */
public class HttpPostDemo {
    public static void main(String[] args) {
        String url = "http://wthrcdn.etouch.cn/weather_mini";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "北京");
        String result1 = HttpUtil.post(url, paramMap);
        Console.log(result1);

        // 链式构建请求
        String result2 = HttpRequest.post(url)
                .header(Header.USER_AGENT, "Hutool http")
                .form(paramMap)
                .execute().body();
        Console.log(result2);
    }
}
