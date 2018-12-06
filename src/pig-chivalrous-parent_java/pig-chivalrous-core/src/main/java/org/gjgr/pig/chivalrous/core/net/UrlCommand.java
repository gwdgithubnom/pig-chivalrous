package org.gjgr.pig.chivalrous.core.net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author gwd
 * @Time 12-05-2018  Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class UrlCommand {

    public static UriBuilder uriBuilder() {
        return new UriBuilder();
    }

    public URI uri(UriBuilder uriBuilder) {
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
