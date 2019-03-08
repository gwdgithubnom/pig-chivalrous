package org.gjgr.pig.chivalrous.core.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.lang.Assert;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author gwd
 * @Time 12-05-2018 Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class UriCommand {
    private static Logger logger = LoggerFactory.getLogger(UriCommand.class);

    public static UriBuilder uriBuilder() {
        return new UriBuilder();
    }

    public static UriBuilder uriBuilder(String url) {
        URI uri = uri(url);
        UriBuilder uriBuilder = uriBuilder();
        uriBuilder.setUserInfo(uri.getUserInfo());
        uriBuilder.setHost(uri.getHost());
        uriBuilder.setPath(uri.getPath());
        uriBuilder.setScheme(uri.getScheme());
        uriBuilder.setPort(uri.getPort());
        uriBuilder.setFragment(uri.getFragment());
        uriBuilder.setCustomQuery(uri.getRawQuery());
        return uriBuilder;
    }

    public static URI uriWithoutQuery(URI uri) {
        String url = uri.toString();
        return uriWithoutQuery(url);
    }

    public static URI uriWithoutQuery(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        return uri(url);
    }

    public static URI uri(UriBuilder uriBuilder) {
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fixIllegalCharacterInUrl(String url) {
        // TODO more charator support
        url = url.replace(" ", "%20").replaceAll("#+", "#").replaceAll("\\r|\\n", "");
        int query = 0;
        if (url.contains("?")) {
            query = url.indexOf("?");
        } else {
            query = url.length();
        }
        String uri = url.substring(0, query);
        if (query != url.length()) {
            String last = url.substring(query + 1);
            String code = last.replaceAll("\\|", "%7C").replaceAll("\\/", "%2F").replaceAll(",", "%2C")
                    .replaceAll(Pattern.quote("\""), "%22").replaceAll("\\{", "%7B").replaceAll("\\}", "%7D")
                    .replaceAll(":", "%3A");
            uri = uri + "?" + code;
        }
        return uri;
    }

    public static URI uri(String url) {
        url = fixIllegalCharacterInUrl(url);
        try {
            return new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String paramString(String url) {
        URI uri;
        url = fixIllegalCharacterInUrl(url);
        try {
            uri = new URI(url);
            String string = uri.getRawQuery();
            return string;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static Map<String, String> param(String param) {
        String[] strings = param.split("&");
        Map<String, String> stringMap = new HashMap<>();
        if (strings.length > 0) {
            for (String s : strings) {
                String[] ss = s.split("=");
                if (ss.length == 2) {
                    stringMap.put(ss[0], ss[1]);
                }
            }
        }
        return stringMap;
    }

    public static Map<String, String> paramMap(String url) {
        try {
            URI uri = new URI(url);
            String string = uri.getRawQuery();
            return param(string);
        } catch (URISyntaxException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 通过一个字符串形式的URL地址创建URL对象
     *
     * @param url URL
     * @return URL对象
     */
    public static URL url(String url) {
        url = fixIllegalCharacterInUrl(url);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new UtilException(e.getMessage(), e);
        }
    }

    /**
     * 获得URL
     *
     * @param pathBaseClassLoader 相对路径（相对于classes）
     * @return URL
     */
    public static URL getURL(String pathBaseClassLoader) {
        return ClassCommand.getClassLoader().getResource(pathBaseClassLoader);
    }

    /**
     * 获得URL
     *
     * @param path 相对给定 class所在的路径
     * @param clazz 指定class
     * @return URL
     */
    public static URL getURL(String path, Class<?> clazz) {
        return clazz.getResource(path);
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param file URL对应的文件对象
     * @return URL
     * @throws UtilException MalformedURLException
     */
    public static URL getURL(File file) {
        Assert.notNull(file, "File is null !");
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UtilException("Error occured when get URL!", e);
        }
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param files URL对应的文件对象
     * @return URL
     * @throws UtilException MalformedURLException
     */
    public static URL[] getURLs(File...files) {
        final URL[] urls = new URL[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                urls[i] = files[i].toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new UtilException("Error occured when get URL!", e);
        }

        return urls;
    }

    /**
     * 格式化URL链接
     *
     * @param url 需要格式化的URL
     * @return 格式化后的URL，如果提供了null或者空串，返回null
     */
    public static String formatUrl(String url) {
        if (StringCommand.isBlank(url)) {
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return "http://" + url;
    }

    /**
     * 补全相对路径
     *
     * @param baseUrl 基准URL
     * @param relativePath 相对URL
     * @return 相对路径
     * @throws UtilException MalformedURLException
     */
    public static String complateUrl(String baseUrl, String relativePath) {
        baseUrl = formatUrl(baseUrl);
        if (StringCommand.isBlank(baseUrl)) {
            return null;
        }

        try {
            final URL absoluteUrl = new URL(baseUrl);
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 编码URL<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
     *
     * @param url URL
     * @param charset 编码
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 解码URL<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param url URL
     * @param charset 编码
     * @return 解码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String decode(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 获得path部分<br>
     * URI -> http://www.aaa.bbb/search?scope=ccc&q=ddd PATH -> /search
     *
     * @param uriStr URI路径
     * @return path
     * @throws UtilException URISyntaxException
     */
    public static String getPath(String uriStr) {
        URI uri = null;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }

        return uri == null ? null : uri.getPath();
    }

    /**
     * 转URL为URI
     *
     * @param url URL
     * @return URI
     * @throws URISyntaxException
     */
    public static URI toURI(URL url) {
        return toURI(url.toString());
    }

    /**
     * 转字符串为URI
     *
     * @param location 字符串路径
     * @return URI
     * @throws URISyntaxException
     */
    public static URI toURI(String location) {
        try {
            return new URI(location.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }
    }
}
