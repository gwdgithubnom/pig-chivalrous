package org.gjgr.pig.chivalrous.web.http.demo;

import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.gjgr.pig.chivalrous.web.http.HttpRequest;
import org.gjgr.pig.chivalrous.web.http.HttpUtil;
import org.gjgr.pig.chivalrous.web.http.ssl.SSLSocketFactoryBuilder;

/**
 * GET请求样例
 *
 * @author Looly
 */
public class HttpGetDemo {
    public static void main(String[] args) {
        String url = "https://www.baidu.com";

        // 方法1：最简单的HTTP请求，可以自动通过header等信息判断编码
        String content = HttpUtil.get(url);
        System.out.println(content);

        // 方法2：从远程直接读取字符串，需要自定义编码，直接调用JDK方法
        String content2 = HttpUtil.downloadString(url, CharsetCommand.UTF_8);
        System.out.println(content2);

        // 方法3：自定义构建HTTP GET请求，发送Http GET请求，针对HTTPS安全加密，可以自定义SSL
        HttpRequest request = HttpRequest
                .get(url)
                // 禁用缓存
                .disableCache()
                // 自定义SSL版本
                .setSSLProtocol(SSLSocketFactoryBuilder.TLSv12);
        System.out.println(request.execute().body());
    }
}
