package org.gjgr.pig.chivalrous.core.net;

/**
 * @Author gwd
 * @Time 01-17-2019 Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public enum UriScheme {

    HTTPS("https"), HTTP("http"), FILE("file");
    // URI = scheme:[//authority]path[?query][#fragment]
    private final String scheme;

    UriScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public String getProtocol() {
        return scheme + ":";
    }

    public String getProtocolWithNoAuthority() {
        return scheme + "://";
    }

    public String getValueWithNoAuthority(String url) {
        if (!url.contains("://")) {
            return scheme + "://" + url;
        } else {
            return url;
        }
    }

    public String getValue(String url) {
        if (!url.contains(":")) {
            return scheme + ":" + url;
        } else {
            return url;
        }
    }

    public String getValue(String authority, String url) {
        if (authority == null) {
            return getValueWithNoAuthority(url);
        } else {
            return getValue(authority + url);
        }
    }
}
