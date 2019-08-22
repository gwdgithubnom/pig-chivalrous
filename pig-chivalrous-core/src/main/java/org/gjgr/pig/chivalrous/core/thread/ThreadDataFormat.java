package org.gjgr.pig.chivalrous.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ThreadDataFormat {
    private static Logger logger = LoggerFactory.getLogger(ThreadDataFormat.class);
    private ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();
    private String pattern = "yyyy-MM-dd HH:mm:ss";
    private String timeZone = "Asia/Shanghai";
    private String locale = "en-US";

    private ThreadDataFormat() {

    }

    private ThreadDataFormat(String pattern) {
        this.pattern = pattern;
    }

    private ThreadDataFormat(String pattern, String timeZone) {
        this(pattern);
        this.timeZone = timeZone;
    }

    private ThreadDataFormat(String pattern, String timeZone, String locale) {
        this(pattern, timeZone);
        this.locale = locale;
    }

    public static ThreadDataFormat getInstance(String pattern) {
        return new ThreadDataFormat(pattern);
    }

    public static ThreadDataFormat getInstance(String pattern, String timeZone) {
        return new ThreadDataFormat(pattern, timeZone);
    }

    public static ThreadDataFormat getInstance(String pattern, String timeZone, String locale) {
        return new ThreadDataFormat(pattern, timeZone, locale);
    }

    private Locale makeLocale() {
        if (locale == null) {
            return Locale.getDefault();
        }
        String[] countryTolang = locale.split("_");
        if (countryTolang.length == 1) {
            return new Locale(locale);
        } else {
            return new Locale(countryTolang[0], countryTolang[1]);
        }
    }

    private DateFormat getDateFormat(boolean refresh) {
        DateFormat df = threadLocal.get();
        if (df == null || refresh) {
            df = new SimpleDateFormat(pattern, makeLocale());
            df.setTimeZone(TimeZone.getTimeZone(timeZone));
            threadLocal.set(df);
        }
        return df;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        getDateFormat(true);
    }

    public void setLocale(String locale) {
        this.locale = locale;
        getDateFormat(true);
    }

    public Date parse(String dateTime) {
        try {
            return getDateFormat(false).parse(dateTime);
        } catch (ParseException e) {
            logger.error("parse {} error, msg {}", dateTime, e.getMessage());
        }
        return null;
    }

    public String format(Date date) {
        return getDateFormat(false).format(date);
    }

}
