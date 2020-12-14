package com.peter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {

    private static final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd-HH");

    static {
        df1.setLenient(false);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        df2.setLenient(false);
        df2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static String format1(Date date) {
        return df1.format(date);
    }

    public static String format2(Date date) {
        return df2.format(date);
    }

    public static Date parseDate(String datastr) {
        return parseDate(datastr, false);
    }

    public static Date parseDate(String datestr, boolean isFrom) {
        Date date = null;
        if (datestr != null) {
            String[] ds = datestr.split("-", -1);
            if (ds.length == 3) {
                try {
                    date = df1.parse(datestr);
                    date.setHours(isFrom ? 0 : 23);
                    return date;
                } catch (ParseException e) {
                    return null;
                }
            }
            if (ds.length == 4) {
                try {
                    return df2.parse(datestr);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return date;
    }

    public static Date incrementOneDayAndGet(Date date) {
        return addAndGet(date, Calendar.DAY_OF_YEAR, 1);
    }

    public static Date incrementOneHourAndGet(Date date) {
        return addAndGet(date, Calendar.HOUR, 1);
    }

    public static Date addAndGet(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
}
