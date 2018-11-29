package org.gjgr.pig.chivalrous.core.clone;

/**
 * 克隆支持类，提供默认的克隆方法
 *
 * @param <T>
 * @author Looly
 */
public class CloneSupport<T> implements Cloneable<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneRuntimeException(e);
        }
    }

}
