package org.gjgr.pig.chivalrous.core.date.format;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * DateTimeFormatter 是一个线程安全的 {@link java.text.SimpleDateFormat} 实现。
 * </p>
 * <p>
 * <p>
 * 通过以下静态方法获得此对象: <br>
 * {@link #getInstance(String, TimeZone, Locale)}<br>
 * {@link #getDateInstance(int, TimeZone, Locale)}<br>
 * {@link #getTimeInstance(int, TimeZone, Locale)}<br>
 * {@link #getDateTimeInstance(int, int, TimeZone, Locale)}
 * </p>
 * <p>
 * Thanks to Apache Commons Lang 3.5
 *
 * @since 2.16.2
 */
public class DateTimeFormatter extends Format implements DateTimeParser, DateTimePrinter {
    /**
     * FULL locale dependent dateTime or time style.
     */
    public static final int FULL = DateFormat.FULL;
    /**
     * LONG locale dependent dateTime or time style.
     */
    public static final int LONG = DateFormat.LONG;
    /**
     * MEDIUM locale dependent dateTime or time style.
     */
    public static final int MEDIUM = DateFormat.MEDIUM;
    /**
     * SHORT locale dependent dateTime or time style.
     */
    public static final int SHORT = DateFormat.SHORT;
    private static final long serialVersionUID = 8097890768636183236L;
    private static final FormatCache<DateTimeFormatter> cache = new FormatCache<DateTimeFormatter>() {
        @Override
        protected DateTimeFormatter createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
            return new DateTimeFormatter(pattern, timeZone, locale);
        }
    };

    private final SimpleDateTimePrinter printer;
    private final SimpleDateTimeParser parser;

    // -----------------------------------------------------------------------

    /**
     * 构造
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @param timeZone 非空时区 {@link TimeZone}
     * @param locale {@link Locale} 日期地理位置
     * @throws NullPointerException if pattern, timeZone, or locale is null.
     */
    protected DateTimeFormatter(final String pattern, final TimeZone timeZone, final Locale locale) {
        this(pattern, timeZone, locale, null);
    }

    /**
     * 构造
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @param timeZone 非空时区 {@link TimeZone}
     * @param locale {@link Locale} 日期地理位置
     * @param centuryStart The start of the 100 year period to use as the "default century" for 2 digit year parsing. If
     *            centuryStart is null, defaults to now - 80 years
     * @throws NullPointerException if pattern, timeZone, or locale is null.
     */
    protected DateTimeFormatter(final String pattern, final TimeZone timeZone, final Locale locale,
            final Date centuryStart) {
        printer = new SimpleDateTimePrinter(pattern, timeZone, locale);
        parser = new SimpleDateTimeParser(pattern, timeZone, locale, centuryStart);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例，使用默认格式和地区
     *
     * @return {@link DateTimeFormatter}
     */
    public static DateTimeFormatter getInstance() {
        return cache.getInstance();
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例，使用默认地区
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @return {@link DateTimeFormatter}
     * @throws IllegalArgumentException 日期格式问题
     */
    public static DateTimeFormatter getInstance(final String pattern) {
        return cache.getInstance(pattern, null, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @param timeZone 时区{@link TimeZone}
     * @return {@link DateTimeFormatter}
     * @throws IllegalArgumentException 日期格式问题
     */
    public static DateTimeFormatter getInstance(final String pattern, final TimeZone timeZone) {
        return cache.getInstance(pattern, timeZone, null);
    }

    // -----------------------------------------------------------------------

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @param locale {@link Locale} 日期地理位置
     * @return {@link DateTimeFormatter}
     * @throws IllegalArgumentException 日期格式问题
     */
    public static DateTimeFormatter getInstance(final String pattern, final Locale locale) {
        return cache.getInstance(pattern, null, locale);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
     * @param timeZone 时区{@link TimeZone}
     * @param locale {@link Locale} 日期地理位置
     * @return {@link DateTimeFormatter}
     * @throws IllegalArgumentException 日期格式问题
     */
    public static DateTimeFormatter getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
        return cache.getInstance(pattern, timeZone, locale);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getDateInstance(final int style) {
        return cache.getDateInstance(style, null, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getDateInstance(final int style, final Locale locale) {
        return cache.getDateInstance(style, null, locale);
    }

    // -----------------------------------------------------------------------

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 时区{@link TimeZone}
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getDateInstance(final int style, final TimeZone timeZone) {
        return cache.getDateInstance(style, timeZone, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 时区{@link TimeZone}
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getDateInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getDateInstance(style, timeZone, locale);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style time style: FULL, LONG, MEDIUM, or SHORT
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getTimeInstance(final int style) {
        return cache.getTimeInstance(style, null, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style time style: FULL, LONG, MEDIUM, or SHORT
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getTimeInstance(final int style, final Locale locale) {
        return cache.getTimeInstance(style, null, locale);
    }

    // -----------------------------------------------------------------------

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone optional time zone, overrides time zone of formatted time
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getTimeInstance(final int style, final TimeZone timeZone) {
        return cache.getTimeInstance(style, timeZone, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param style time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone optional time zone, overrides time zone of formatted time
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getTimeInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getTimeInstance(style, timeZone, locale);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param dateStyle dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     * @since 2.1
     */
    public static DateTimeFormatter getDateTimeInstance(final int dateStyle, final int timeStyle) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param dateStyle dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     * @since 2.1
     */
    public static DateTimeFormatter getDateTimeInstance(final int dateStyle, final int timeStyle, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, locale);
    }

    // ----------------------------------------------------------------------- Constructor start

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param dateStyle dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 时区{@link TimeZone}
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     * @since 2.1
     */
    public static DateTimeFormatter getDateTimeInstance(final int dateStyle, final int timeStyle,
            final TimeZone timeZone) {
        return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
    }

    /**
     * 获得 {@link DateTimeFormatter} 实例
     *
     * @param dateStyle dateTime style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 时区{@link TimeZone}
     * @param locale {@link Locale} 日期地理位置
     * @return 本地化 {@link DateTimeFormatter}
     * @throws IllegalArgumentException
     */
    public static DateTimeFormatter getDateTimeInstance(final int dateStyle, final int timeStyle,
            final TimeZone timeZone, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    }
    // ----------------------------------------------------------------------- Constructor end

    // ----------------------------------------------------------------------- Format methods
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        return toAppendTo.append(printer.format(obj));
    }

    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parser.parseObject(source, pos);
    }

    @Override
    public String format(final long millis) {
        return printer.format(millis);
    }

    @Override
    public String format(final Date date) {
        return printer.format(date);
    }

    @Override
    public String format(final Calendar calendar) {
        return printer.format(calendar);
    }

    @Override
    public <B extends Appendable> B format(final long millis, final B buf) {
        return printer.format(millis, buf);
    }

    @Override
    public <B extends Appendable> B format(final Date date, final B buf) {
        return printer.format(date, buf);
    }

    @Override
    public <B extends Appendable> B format(final Calendar calendar, final B buf) {
        return printer.format(calendar, buf);
    }

    // ----------------------------------------------------------------------- Parsing
    @Override
    public Date parse(final String source) throws ParseException {
        return parser.parse(source);
    }

    @Override
    public Date parse(final String source, final ParsePosition pos) {
        return parser.parse(source, pos);
    }

    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        return parser.parse(source, pos, calendar);
    }

    // ----------------------------------------------------------------------- Accessors
    @Override
    public String getPattern() {
        return printer.getPattern();
    }

    @Override
    public TimeZone getTimeZone() {
        return printer.getTimeZone();
    }

    @Override
    public Locale getLocale() {
        return printer.getLocale();
    }

    /**
     * 估算生成的日期字符串长度<br>
     * 实际生成的字符串长度小于或等于此值
     *
     * @return 日期字符串长度
     */
    public int getMaxLengthEstimate() {
        return printer.getMaxLengthEstimate();
    }

    @Override
    public int hashCode() {
        return printer.hashCode();
    }

    // Basics
    // -----------------------------------------------------------------------
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DateTimeFormatter == false) {
            return false;
        }
        final DateTimeFormatter other = (DateTimeFormatter) obj;
        // no need to check parser, as it has same invariants as printer
        return printer.equals(other.printer);
    }

    @Override
    public String toString() {
        return "DateTimeFormatter[" + printer.getPattern() + "," + printer.getLocale() + ","
                + printer.getTimeZone().getID() + "]";
    }
}
