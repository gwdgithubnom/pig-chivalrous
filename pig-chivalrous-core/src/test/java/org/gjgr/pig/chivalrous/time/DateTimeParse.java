package org.gjgr.pig.chivalrous.time;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author gwd
 * @Time 07-04-2019  Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class DateTimeParse {
    @Test
    public void timeParse() {
        String language = "en";
        String country = "us";
        String zoneId = "Europe/Moscow";
        String pattern = null;
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(zoneId));
        Locale locale = new Locale(language, country);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);

//        TimeZone timeZone = new SimpleTimeZone("");
//        simpleDateFormat.setTimeZone(timeZone);
//        Calendar calendar = Calendar.getInstance();
//

    }
}
