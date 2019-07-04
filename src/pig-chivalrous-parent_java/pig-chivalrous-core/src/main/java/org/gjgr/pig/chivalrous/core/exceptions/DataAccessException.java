package org.gjgr.pig.chivalrous.core.exceptions;

import java.net.URISyntaxException;

/**
 * @Author gwd
 * @Time 11-27-2018 Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class DataAccessException extends NestedRuntimeException {
    public DataAccessException(String msg, URISyntaxException exception) {
        super(msg, exception);
    }
}
