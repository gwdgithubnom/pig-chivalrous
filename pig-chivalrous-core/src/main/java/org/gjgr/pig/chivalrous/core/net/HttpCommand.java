package org.gjgr.pig.chivalrous.core.net;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
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

    protected static Message messageReturn(Integer code, String url, String type) {
        Message message = new Message();
        message.getInfo().put("url", url);
        message.setType(type);
        message.setTimestamp(System.currentTimeMillis());
        message.setCode(code);
        return message;
    }

    public static Message httpURLConnection(HttpURLConnection httpURLConnection, String data)
            throws IOException {
        Message message = messageReturn(200, httpURLConnection.getURL().toString(), "simple");
        DataInputStream dataInputStream = null;
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

    public static Message get(String url, JsonObject data, String urlParms, Map<String, String> params,
            Map<String, String> headers) {
        Message message = messageReturn(200, url, "get");
        message.setDatum(data);
        GetMethod getMethod = new GetMethod(url);
        if (headers != null) {
            headers.forEach((k, v) -> {
                getMethod.addRequestHeader(k, v);
            });
            message.getInfo().put("header", headers);
        }
        if (urlParms != null) {
            getMethod.setQueryString(
                    (getMethod.getQueryString() == null ? "" : getMethod.getQueryString()) + "&" + urlParms);
        }
        if (data != null) {
            JsonObject jsonObject = data.getAsJsonObject();
            Iterator<String> iterator = jsonObject.keySet().iterator();
            if (params == null) {
                params = new HashMap<>();
            }
            while (iterator.hasNext()) {
                String key = iterator.next();
                params.put(key, jsonObject.get(key).toString());
            }
        }
        if (params != null) {
            StringBuffer stringBuffer =
                    new StringBuffer(getMethod.getQueryString() == null ? "" : getMethod.getQueryString() + "&");
            for (Map.Entry<String, String> item : params.entrySet()) {
                stringBuffer.append(item.getKey() + "=" + item.getValue() + "&");
            }
            getMethod.setQueryString(stringBuffer.toString());
            message.getInfo().put("param", params);
        }
        HttpClient httpClient = new HttpClient();
        try {
            int response = httpClient.executeMethod(getMethod);
            message.setCode(response);
            String result = getMethod.getResponseBodyAsString();
            message.setData(result);
        } catch (IOException e) {
            message.setMessage(e.getMessage());
            if (message.getCode() == 200) {
                message.setCode(-1);
            }
            e.printStackTrace();
        }
        return message;
    }

    public static Message post(String url, JsonElement data,Map<String, String> formParams, Map<String, String> params, Map<String, String> headers) {
        Message message = messageReturn(200, url, "post");
        message.setDatum(data);
        message.getInfo().put("url", url);
        PostMethod postMethod = new PostMethod(url);
        // postMethod.setRequestHeader("Content-Type", "application/json");
        try {
            if(data!=null){
                if (headers.containsKey("Content-Type")) {
                    postMethod.setRequestEntity(new StringRequestEntity(data.toString(), headers.get("Content-Type"), "UTF-8"));
                } else if (headers.containsKey("content-type")) {
                    postMethod.setRequestEntity(new StringRequestEntity(data.toString(), headers.get("Content-Type"), "UTF-8"));
                } else {
                    postMethod.setRequestEntity(new StringRequestEntity(data.toString(), "application/json", "UTF-8"));
                }
            }
            if(formParams!=null){
                List<NameValuePair> nameValuePairList = new LinkedList<>();
                formParams.forEach((k, v) -> {
                    nameValuePairList.add(new NameValuePair(k,v));
                });
                postMethod.setRequestBody(nameValuePairList.toArray(new NameValuePair[1]));
            }
        } catch (UnsupportedEncodingException e) {
            message.setMessage(e.getMessage());
            e.printStackTrace();
        }
        // postMethod.setRequestBody(params);
        if (params != null) {
            HttpMethodParams httpMethodParams = new HttpMethodParams();
            params.forEach((k, v) -> {
                httpMethodParams.setParameter(k, v);
            });
            postMethod.setParams(httpMethodParams);
            message.getInfo().put("param", params);
        }
        if (headers != null) {
            headers.forEach((k, v) -> {
                postMethod.addRequestHeader(k, v);
            });
            message.getInfo().put("header", headers);
        }
        HttpClient httpClient = new HttpClient();
        try {
            int response = httpClient.executeMethod(postMethod);
            message.setCode(response);
            String result = postMethod.getResponseBodyAsString();
            message.setData(result);
        } catch (IOException e) {
            message.setMessage(e.getMessage());
            message.setCode(-1);
            e.printStackTrace();
        }
        return message;
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
