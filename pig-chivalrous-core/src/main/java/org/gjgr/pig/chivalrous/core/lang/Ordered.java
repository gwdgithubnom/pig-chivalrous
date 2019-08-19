package org.gjgr.pig.chivalrous.core.lang;

/**
 * @Author gwd
 * @Time 12-12-2018  Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */

/**
 * Interface to be implemented by objects that should be orderable, such as with a {@link java.util.Collection}.
 */
public interface Ordered {
    /**
     * The highest precedence
     */
    int HIGHEST = Integer.MIN_VALUE;

    /**
     * The lowest precedence
     */
    int LOWEST = Integer.MAX_VALUE;

    /**
     * Gets the order.
     * <p/>
     * Use low numbers for higher priority. Normally the sorting will start from 0 and move upwards. So if you want to
     * be last then use {@link Integer#MAX_VALUE} or eg {@link #LOWEST}.
     *
     * @return the order
     */
    int getOrder();
}
