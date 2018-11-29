package org.gjgr.pig.chivalrous.core.date;

/**
 * 季度枚举<br>
 *
 * @author Looly
 * @see #SPRING
 * @see #SUMMER
 * @see #ANTUMN
 * @see #WINTER
 */
public enum PigSeason {

    /**
     * 春季（第一季度）
     */
    SPRING(1),
    /**
     * 夏季（第二季度）
     */
    SUMMER(2),
    /**
     * 秋季（第三季度）
     */
    ANTUMN(3),
    /**
     * 冬季（第四季度）
     */
    WINTER(4);

    // ---------------------------------------------------------------
    private int value;

    private PigSeason(int value) {
        this.value = value;
    }

    /**
     * 将 季度int转换为Season枚举对象<br>
     *
     * @param intValue 季度int表示
     * @return {@link PigSeason}
     * @see #SPRING
     * @see #SUMMER
     * @see #ANTUMN
     * @see #WINTER
     */
    public static PigSeason of(int intValue) {
        switch (intValue) {
            case 1:
                return SPRING;
            case 2:
                return SUMMER;
            case 3:
                return ANTUMN;
            case 4:
                return WINTER;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
