package org.gjgr.pig.chivalrous.core.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.gjgr.pig.chivalrous.core.entity.Message;
import org.gjgr.pig.chivalrous.core.entity.MessageBuilder;
import org.gjgr.pig.chivalrous.core.io.stream.StreamCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author gwd
 * @Time 12-08-2018 Saturday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class HttpCommand {
    private static Logger logger = LoggerFactory.getLogger(HttpCommand.class);

    public static CookieStore cookieStore(HttpClientBuilder httpClientBuilder, Map<String, String> stringMap,
                                          String domain) {
        if (stringMap == null) {
            httpClientBuilder.disableCookieManagement();
            return null;
        } else {
            CookieStore cookieStore = cookieStore(stringMap, domain);
            httpClientBuilder.setDefaultCookieStore(cookieStore);
            return cookieStore;
        }
    }

    public static CookieStore cookieStore(Map<String, String> stringMap, String domain) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : stringMap.entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(domain);
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }

    public static CookieStore cookieStore(CookieStore cookieStore, Map<String, String> stringMap) {
        for (Map.Entry<String, String> cookieEntry : stringMap.entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }

    public static CookieStore cookieStore(CookieStore cookieStore, Map<String, String> stringMap, String domain) {
        for (Map.Entry<String, String> cookieEntry : stringMap.entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(domain);
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }

    public static Message httpURLConnection(HttpURLConnection httpURLConnection, String data)
            throws IOException {
        DataInputStream dataInputStream = null;
        Message message = new Message();
        message.getInfo().put("url", httpURLConnection.toString());
        message.getInfo().put("data", data);
        if (!httpURLConnection.getRequestProperties().containsKey("Content-Language")) {
            httpURLConnection.setRequestProperty("Content-Language", "en-US");
        }
        if (httpURLConnection.getRequestProperty("Content-Type") == null) {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        } else if (httpURLConnection.getRequestProperty("content-type") == null) {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);
        if (data != null) {
            DataOutputStream wr = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            byte[] bytes = data.getBytes("UTF-8");
            wr.write(bytes);
            wr.flush();
            wr.close();
        }
        message.setCode(httpURLConnection.getResponseCode());
        message.setMessage(httpURLConnection.getResponseMessage());
        message.setVersion(httpURLConnection.getDate() + "");
        try {
            dataInputStream = new DataInputStream(httpURLConnection.getInputStream());
            if (dataInputStream == null) {
                dataInputStream = new DataInputStream(httpURLConnection.getErrorStream());
            }
            if (dataInputStream != null) {
                data = StreamCommand.loadText(dataInputStream);
                message.setData(data);
            }
            logger.debug("for new session sum submit:{}, request response:{} ", httpURLConnection.getResponseCode(),
                    dataInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("for new session sum submit:{}, request response:{}, set data:{} ",
                    httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        message.setDatum(httpURLConnection.toString());
        return message;
    }

    public static Message get(String targetURL) throws IOException {
        URL url = new URL(targetURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        Message message = httpURLConnection(httpURLConnection, null);
        return message;
    }

    public static Message post(String targetURL, String data, String contentType, int timeOut) throws IOException {
        URL url = new URL(targetURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        if (contentType != null) {
            connection.setRequestProperty("Content-Type", contentType);
        }
        if (data != null) {
            connection.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
        }
        if (timeOut > 0) {
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
        }
        Message dataInputStream = httpURLConnection(connection, data);
        return dataInputStream;
    }

    public static Message post(String targetURL, String data, String contentType) throws IOException {
        return post(targetURL, data, contentType, 0);
    }

    public static Message doGet(String targetURL) {
        Message data = null;
        logger.info("try to send a post request in octet stream, by url:{}, with query:{}", targetURL, data);
        try {
            data = get(targetURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("send a post request in octet stream, by url:{}, data:{}", targetURL, data);
        return data;
    }

    public static Message doPost(String targetURL, String data) {
        logger.info("try to send a post request in octet stream, by url:{}, with query:{}", targetURL, data);
        Message message = null;
        try {
            message = post(targetURL, data, null);
        } catch (Exception e) {
            e.printStackTrace();
            message = MessageBuilder.error(900, e.getMessage());
        }
        logger.info("send a post request in octet stream, by url:{}, data:{}", targetURL, data);
        return message;
    }

    public static Message doPostInxWWWFormUrlencoded(String targetURL, String data) {
        logger.info("try to send a post request in octet stream, by url:{}, with query:{}", targetURL, data);
        Message message;
        try {
            message = post(targetURL, data, "x-www-form-urlencoded");
        } catch (Exception e) {
            message = MessageBuilder.exception(900, e.getMessage());
            e.printStackTrace();
        }
        logger.info("send a post request in octet stream, by url:{}, data:{}", targetURL, data);
        return message;
    }

    public static Message doPostInxMultipartFormData(String targetURL, String data) {
        logger.info("try to send a post request in octet stream, by url:{}, with query:{}", targetURL, data);
        Message message;
        try {
            message = post(targetURL, data, "multipart/form-data");
        } catch (Exception e) {
            message = MessageBuilder.exception(900, e.getMessage());
            e.printStackTrace();
        }
        logger.info("send a post request in octet stream, by url:{}, data:{}", targetURL, data);
        return message;
    }

    public static Message doPostInOctetStream(String targetURL, String data) {
        logger.info("try to send a post request in octet stream, by url:{}, with query:{}", targetURL, data);
        Message message;
        try {
            message = post(targetURL, data, "application/octet-stream");
        } catch (Exception e) {
            message = MessageBuilder.exception(900, e.getMessage());
            e.printStackTrace();
        }
        logger.info("send a post request in octet stream, by url:{}, data:{}", targetURL, data);
        return message;
    }

}
