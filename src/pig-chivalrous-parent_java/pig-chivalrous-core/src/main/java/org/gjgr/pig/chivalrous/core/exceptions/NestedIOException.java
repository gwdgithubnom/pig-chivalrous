package org.gjgr.pig.chivalrous.core.exceptions;

/**
 * @Author gwd
 * @Time 01-25-2019 Friday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class NestedIOException extends RuntimeException {
    public NestedIOException(String msg, Exception exception) {
        super(msg, exception);
    }
}