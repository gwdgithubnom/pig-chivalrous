package org.gjgr.pig.chivalrous.core.exceptions;

/**
 * @Author gwd
 * @Time 12-12-2018 Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
@FunctionalInterface
public interface ThrowingFunction<I, R, T extends Throwable> {
    R apply(I in) throws T;
}
