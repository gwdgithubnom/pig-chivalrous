package org.gjgr.pig.chivalrous.core.lang;

/**
 * 匹配接口
 *
 * @param <T>
 * @author Looly
 */
public interface Matcher<T> {
    /**
     * 给定对象是否匹配
     *
     * @param t 对象
     * @return 是否匹配
     */
    public boolean match(T t);
}
