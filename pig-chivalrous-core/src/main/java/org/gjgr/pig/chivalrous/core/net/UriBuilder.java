package org.gjgr.pig.chivalrous.core.net;

import java.io.Serializable;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.gjgr.pig.chivalrous.core.crypto.CryptoCommand;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author gwd
 * @Time 12-05-2018 Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class UriBuilder implements Serializable {

    private String scheme;
    private String encodedSchemeSpecificPart;
    private String encodedAuthority;
    private String userInfo;
    private String encodedUserInfo;
    private String host;
    private int port;
    private String path;
    private String encodedPath;
    private String encodedQuery;
    private List<NameValuePair> queryParams;
    private String query;
    private Charset charset;
    private String fragment;
    private String encodedFragment;

    /**
     * Constructs an empty instance.
     */
    public UriBuilder() {
        super();
        this.port = -1;
    }

    /**
     * Construct an instance from the string which must be a valid URI.
     *
     * @param string a valid URI in string form
     * @throws URISyntaxException if the input is not a valid URI
     */
    public UriBuilder(final String string) throws URISyntaxException {
        super();
        try {
            digestURI(new URI(string));
        } catch (Exception e) {
            try {
                String url = UriCommand.fixIllegalCharacterInUrl(string);
                digestURI(new URI(url));
            } catch (Exception e1) {
                try {
                    digestURI(new URI(URLEncoder.encode(string, Consts.UTF_8.name())));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * Construct an instance from the provided URI.
     *
     * @param uri
     */
    public UriBuilder(final URI uri) {
        super();
        digestURI(uri);
    }

    private static String normalizePath(final String path, final boolean relative) {
        String s = path;
        if (TextUtils.isBlank(s)) {
            return "";
        }
        int n = 0;
        for (; n < s.length(); n++) {
            if (s.charAt(n) != '/') {
                break;
            }
        }
        if (n > 1) {
            s = s.substring(n - 1);
        }
        if (!relative && !s.startsWith("/")) {
            s = "/" + s;
        }
        return s;
    }

    public UriBuilder paramOrdered() {
        List<NameValuePair> params = getQueryParams();
        if (params != null) {
            params.sort(new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair o1, NameValuePair o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        setParameters(params);
        return this;
    }

    /**
     * @since 4.4
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @since 4.4
     */
    public UriBuilder setCharset(final Charset charset) {
        this.charset = charset;
        return this;
    }

    private List<NameValuePair> parseQuery(final String query, final Charset charset) {
        if (query != null && !query.isEmpty()) {
            return URLEncodedUtils.parse(query, charset);
        }
        return null;
    }

    private List<NameValuePair> parseQuery(final String query) {
        if (query != null && !query.isEmpty()) {
            return URLEncodedUtils.parse(query, charset);
        }
        return null;
    }

    public String addQuery(final String key, final String value) {
        NameValuePair nameValuePair = new BasicNameValuePair(key, value);
        String str;
        if (this.query != null) {
            str = URLEncodedUtils.format(nameValuePair, false, false);
            this.query = this.query + "&" + str;
        } else if (encodedQuery != null) {
            str = URLEncodedUtils.format(nameValuePair, true, true);
        } else {
            str = URLEncodedUtils.format(nameValuePair, false, false);
            this.query = "?" + str;
        }
        return str;
    }

    /**
     * Builds a {@link URI} instance.
     */
    public URI build() throws URISyntaxException {
        return new URI(buildString(false));
    }

    public URL buildURL() {
        try {
            return build().toURL();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String param() {
        return param(false, false);
    }

    public String param(boolean blankAsPlus, boolean hexAsPlus) {
        final StringBuilder sb = new StringBuilder();
        if (this.encodedQuery != null) {
            sb.append("?").append(this.encodedQuery);
        } else if (this.queryParams != null && !this.queryParams.isEmpty()) {
            sb.append("?").append(encodeUrlForm(this.queryParams, blankAsPlus, hexAsPlus));
        } else if (this.query != null) {
            sb.append("?").append(encodeUric(this.query));
        }
        return sb.toString();
    }

    public String url() {
        return buildString(false, false);
    }

    public String uri() {
        return toString();
    }

    public String buildString() {
        return buildString(false);
    }

    public String buildString(boolean blankAsPlus, boolean hexAsPlus) {
        final StringBuilder sb = new StringBuilder();
        if (this.scheme != null) {
            sb.append(this.scheme).append(':');
        }
        if (this.encodedSchemeSpecificPart != null) {
            sb.append(this.encodedSchemeSpecificPart);
        } else {
            if (this.encodedAuthority != null) {
                sb.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                sb.append("//");
                if (this.encodedUserInfo != null) {
                    sb.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    sb.append(encodeUserInfo(this.userInfo)).append("@");
                }
                if (InetAddressUtils.isIPv6Address(this.host)) {
                    sb.append("[").append(this.host).append("]");
                } else {
                    sb.append(this.host);
                }
                if (this.port >= 0) {
                    sb.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                sb.append(normalizePath(this.encodedPath, sb.length() == 0));
            } else if (this.path != null) {
                sb.append(encodePath(normalizePath(this.path, sb.length() == 0)));
            }
            sb.append(param(blankAsPlus, hexAsPlus));
        }
        if (this.encodedFragment != null) {
            sb.append("#").append(this.encodedFragment);
        } else if (this.fragment != null) {
            sb.append("#").append(encodeUric(this.fragment));
        }
        return sb.toString();
    }

    private String buildString(boolean blankAsPlus) {
        return buildString(blankAsPlus, false);
    }

    private void digestURI(final URI uri) {
        this.scheme = uri.getScheme();
        this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        this.encodedAuthority = uri.getRawAuthority();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.encodedUserInfo = uri.getRawUserInfo();
        this.userInfo = uri.getUserInfo();
        this.encodedPath = uri.getRawPath();
        this.path = uri.getPath();
        this.encodedQuery = uri.getRawQuery();
        this.queryParams = parseQuery(uri.getRawQuery(), this.charset != null ? this.charset : Consts.UTF_8);
        this.encodedFragment = uri.getRawFragment();
        this.fragment = uri.getFragment();
    }

    private String encodeUserInfo(final String userInfo) {
        return URLEncodedUtils.encUserInfo(userInfo, this.charset != null ? this.charset : Consts.UTF_8);
    }

    private String encodePath(final String path) {
        return URLEncodedUtils.encPath(path, this.charset != null ? this.charset : Consts.UTF_8);
    }

    private String encodeUrlForm(final List<NameValuePair> params, boolean blankAsPlus, boolean hexAsPlus) {
        return URLEncodedUtils.format(params, this.charset != null ? this.charset : Consts.UTF_8, blankAsPlus,
                hexAsPlus);
    }

    private String encodeUrlForm(final List<NameValuePair> params, boolean blankAsPlus) {
        return encodeUrlForm(params, blankAsPlus, true);
    }

    private String encodeUric(final String fragment) {
        return URLEncodedUtils.encUric(fragment, this.charset != null ? this.charset : Consts.UTF_8);
    }

    /**
     * Sets URI user info as a combination of username and password. These values are expected to be unescaped and may
     * contain non ASCII characters.
     */
    public UriBuilder setUserInfo(final String username, final String password) {
        return setUserInfo(username + ':' + password);
    }

    /**
     * Removes URI query.
     */
    public UriBuilder removeQuery() {
        this.queryParams = null;
        this.query = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Sets URI query.
     * <p>
     * The value is expected to be encoded form data.
     *
     * @see URLEncodedUtils#parse
     * @deprecated (4.3) use {@link #setParameters(List)} or {@link #setParameters(NameValuePair...)}
     */
    @Deprecated
    public UriBuilder setQuery(final String query) {
        this.queryParams = parseQuery(query, this.charset != null ? this.charset : Consts.UTF_8);
        this.query = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    public String getFileName() {
        String name = FilenameUtils.getName(getPath());
        return name;
    }

    public String fileName() {
        String name = getFileName();
        if (name == null) {
            name = CryptoCommand.md5(getPath());
        }
        return name;
    }

    /**
     * Sets URI query parameters. The parameter name / values are expected to be unescaped and may contain non ASCII
     * characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove custom
     * query if present.
     * </p>
     *
     * @since 4.3
     */
    public UriBuilder setParameters(final List<NameValuePair> nvps) {
        this.queryParams = new ArrayList<>();
        this.queryParams.addAll(nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Adds URI query parameters. The parameter name / values are expected to be unescaped and may contain non ASCII
     * characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove custom
     * query if present.
     * </p>
     *
     * @since 4.3
     */
    public UriBuilder addParameters(final List<NameValuePair> nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<NameValuePair>();
        }
        if (encodedQuery != null) {
            nvps.addAll(parseQuery(this.encodedQuery));
            this.encodedQuery = null;
        } else if (this.query != null) {
            nvps.addAll(parseQuery(query));
            this.query = null;
        }
        this.queryParams.addAll(nvps);
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    public boolean removeParameter(final String key) {
        boolean status = false;
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<NameValuePair>();
        }
        if (encodedQuery != null) {
            this.queryParams.addAll(parseQuery(this.encodedQuery));
            this.encodedQuery = null;
        } else if (this.query != null) {
            this.queryParams.addAll(parseQuery(this.query));
            this.query = null;
        }

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (NameValuePair nameValuePair : this.queryParams) {
            if (nameValuePair.getName().equalsIgnoreCase(key)) {
                nameValuePairs.add(nameValuePair);
                status = true;
            }
        }
        for (NameValuePair nameValuePair : nameValuePairs) {
            this.queryParams.remove(nameValuePair);
        }
        return status;
    }

    /**
     * Sets URI query parameters. The parameter name / values are expected to be unescaped and may contain non ASCII
     * characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove custom
     * query if present.
     * </p>
     *
     * @since 4.3
     */
    public UriBuilder setParameters(final NameValuePair... nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<NameValuePair>();
        } else {
            this.queryParams.clear();
        }
        for (final NameValuePair nvp : nvps) {
            this.queryParams.add(nvp);
        }
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Adds parameter to URI query. The parameter name and value are expected to be unescaped and may contain non ASCII
     * characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove custom
     * query if present.
     * </p>
     */
    public UriBuilder addParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<NameValuePair>();
        }
        if (encodedQuery != null) {
            this.queryParams.addAll(parseQuery(this.encodedQuery));
            this.encodedQuery = null;
        } else if (this.query != null) {
            this.queryParams.addAll(parseQuery(query));
            this.query = null;
        }
        this.queryParams.add(new BasicNameValuePair(param, value));
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    public UriBuilder addParam(final String param, final String value) {
        Map<String, NameValuePair> cache = new HashMap<>();
        addParameter(param, value);
        if (this.queryParams.size() != 0) {
            this.queryParams.forEach(nameValuePair -> {
                cache.put(nameValuePair.getName(), nameValuePair);
            });
        }
        if (this.queryParams.size() != cache.size()) {
            this.queryParams.clear();
            cache.forEach((k, v) -> {
                this.queryParams.add(v);
            });
        }
        cache.clear();
        return this;
    }

    /**
     * Sets parameter of URI query overriding existing value if set. The parameter name and value are expected to be
     * unescaped and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove custom
     * query if present.
     * </p>
     */
    public UriBuilder setParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<NameValuePair>();
        }
        if (!this.queryParams.isEmpty()) {
            for (final Iterator<NameValuePair> it = this.queryParams.iterator(); it.hasNext(); ) {
                final NameValuePair nvp = it.next();
                if (nvp.getName().equals(param)) {
                    it.remove();
                }
            }
        }
        this.queryParams.add(new BasicNameValuePair(param, value));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Clears URI query parameters.
     *
     * @since 4.3
     */
    public UriBuilder clearParameters() {
        this.queryParams = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Sets custom URI query. The value is expected to be unescaped and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method will remove query
     * parameters if present.
     * </p>
     *
     * @since 4.3
     */
    public UriBuilder setCustomQuery(final String query) {
        this.query = query;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.queryParams = null;
        return this;
    }

    /**
     * @since 4.3
     */
    public boolean isAbsolute() {
        return this.scheme != null;
    }

    /**
     * @since 4.3
     */
    public boolean isOpaque() {
        return this.path == null;
    }

    public String getScheme() {
        return this.scheme;
    }

    public UriBuilder setScheme(UriScheme uriScheme) {
        this.scheme = uriScheme.getScheme();
        return this;
    }

    /**
     * Sets URI scheme.
     */
    public UriBuilder setScheme(final String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    /**
     * Sets URI user info. The value is expected to be unescaped and may contain non ASCII characters.
     */
    public UriBuilder setUserInfo(final String userInfo) {
        this.userInfo = userInfo;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        this.encodedUserInfo = null;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    /**
     * Sets URI host.
     */
    public UriBuilder setHost(final String host) {
        this.host = host;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    /**
     * Sets URI port.
     */
    public UriBuilder setPort(final int port) {
        this.port = port < 0 ? -1 : port;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public String getPath() {
        return this.path;
    }

    /**
     * Sets URI path. The value is expected to be unescaped and may contain non ASCII characters.
     */
    public UriBuilder setPath(final String path) {
        this.path = path;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    public List<NameValuePair> getQueryParams() {
        if (this.queryParams != null) {
            return new ArrayList<NameValuePair>(this.queryParams);
        } else if (query != null) {
            this.queryParams = parseQuery(this.query);
            return this.queryParams;
        } else {
            return new ArrayList<NameValuePair>();
        }
    }


    public Map<String, String> getParams() {
        List<NameValuePair> nameValuePairs = getQueryParams();
        Map<String, String> data = new HashMap<>();
        for (NameValuePair nameValuePair : nameValuePairs) {
            data.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return data;
    }


    public String getFragment() {
        return this.fragment;
    }

    /**
     * Sets URI fragment. The value is expected to be unescaped and may contain non ASCII characters.
     */
    public UriBuilder setFragment(final String fragment) {
        this.fragment = fragment;
        this.encodedFragment = null;
        return this;
    }

    @Override
    public String toString() {
        return buildString(false, false);
    }

}
