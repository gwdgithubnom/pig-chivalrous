package org.gjgr.pig.chivalrous.core.lang;

import org.gjgr.pig.chivalrous.core.mutable.MutableObj;

/**
 * 为不可变的对象引用提供一个可变的包装，在java中支持引用传递。
 *
 * @param <T> 所持有值类型
 * @author Looly
 */
public final class Holder<T> extends MutableObj<T> {
    private static final long serialVersionUID = -3119568580130118011L;

    /**
     * 构造
     */
    public Holder() {
        super();
    }

    // --------------------------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param value
     */
    public Holder(T value) {
        super(value);
    }

    /**
     * 新建Holder类，持有指定值，当值为空时抛出空指针异常
     *
     * @param value 值，不能为空
     * @return Holder
     */
    public static <T> Holder<T> of(T value) throws NullPointerException {
        if (null == value) {
            throw new NullPointerException("Holder can not hold a null value!");
        }
        return new Holder<>(value);
    }
    // --------------------------------------------------------------------------- Constructor end
}
