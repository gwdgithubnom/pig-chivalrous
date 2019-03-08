package org.gjgr.pig.chivalrous.core.exceptions;

import java.net.URISyntaxException;

/**
 * @Author gwd
 * @Time 11-27-2018 Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class NestedRuntimeException extends RuntimeException {
    public NestedRuntimeException(String msg, URISyntaxException exception) {
        super(msg, exception);
    }
}
