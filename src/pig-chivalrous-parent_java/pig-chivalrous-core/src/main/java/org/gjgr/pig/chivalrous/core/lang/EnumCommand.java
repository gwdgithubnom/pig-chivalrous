package org.gjgr.pig.chivalrous.core.lang;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class EnumCommand {

    /**
     * 将字符串转换为 EnumSet. <br>
     * 例如： 对于
     *
     * <pre>
     *  <code>
     * public enum Type { A, B, C, D }
     * 方法 parse(Type.class, "A;B", ";") 将返回一个 EnumSet<Type>, 其中包括 Type.A, Type.B
     * </pre>
     *
     * @param <E>
     * @param clazz
     * @param input
     * @param separator
     * @return
     */
    public <E extends Enum<E>> EnumSet<E> parse(Class<E> clazz, String input, String separator) {
        Validate.notNull(input, "input");
        Validate.notNull(separator, "separator");

        String[] values = input.split(separator);
        List<E> list = new ArrayList<E>();
        for (String value : values) {
            list.add(Enum.valueOf(clazz, value.trim()));
        }

        return EnumSet.copyOf(list);
    }

    public int intEnum(String string) {
        return Objects.hashCode(string);
    }

    /**
     * Command used enum about Long value. first start 0 1 2 is system default used. first start 3 4 5 6 7 8 9 is used
     * define.
     */
    public enum LongEnum {
        Never(1L);
        private final Long value;

        // 9,2 23, 37 2,0 36, 85 4,7 75, 80 7
        LongEnum(final Long value) {
            this.value = value;
        }

        LongEnum(final String value) {
            try {
                this.value = 0L;
            } catch (Exception e) {
                throw new UnsupportedOperationException("could not build enum. length should not more than 9");
            }

        }

        public static String getEnumKey(Long key) {
            for (LongEnum e : LongEnum.values()) {
                if (key.equals(e.value)) {
                    return e.name();
                }
            }
            return null;
        }

        public Long getValue() {
            return value;
        }

        public Long getNegativeValue() {
            return -value;
        }

        public String getKey() {
            return name();
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return getValue() + "";
        }
    }

    /**
     * Command used enum about Double value
     */
    public enum DoubleEnum {
        Never(-1D);
        private final Double value;

        private DoubleEnum(final Double value) {
            this.value = value;
        }

        public static String getEnumKey(Double key) {
            for (LongEnum e : LongEnum.values()) {
                if (key.equals(e.value)) {
                    return e.name();
                }
            }
            return null;
        }

        public Double getValue() {
            return value;
        }

        public Double getNegativeValue() {
            return -value;
        }

        public String getKey() {
            return name();
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return getValue() + "";
        }
    }

    /**
     * About Above Across After Against Along Amidst Among Around As At Before Behind Below Beneath Beside Besides
     * Between Beyond But By Concerning Despite Down During Except Excepting For From In Inside Into Like Near Of Off On
     * Onto Out of Outside Over Past Round Since Than Through Till To Toward Under Until Up Upon With Within Without
     * Command used enum about String value
     */
    public enum StringEnum {
        Never("Never");
        private final String value;

        private StringEnum(final String value) {
            this.value = value;
        }

        public static String getEnumKey(String key) {
            for (LongEnum e : LongEnum.values()) {
                if (key.equals(e.value)) {
                    return e.name();
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public String getNegativeValue() {
            return new StringBuilder(getValue()).reverse().toString();
        }

        public String getKey() {
            return name();
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return getValue() + "";
        }
    }

    // Class.getEnumConstants()
}
