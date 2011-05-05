package com.baleksan.util.compression;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class DateReducerConverter {
    private static final long MSEC_HOUR = 1000 * 60 * 60;
    private static final long MSEC_DAY = MSEC_HOUR * 24;

    public static int reduceToDay(long value) {
        return (short) (value / MSEC_DAY);
    }

    //MMDDYYYY to UTC
    public static long toFacetDateFormat2UTC(String facetDate) {
        String month = facetDate.substring(0, 2);
        String day = facetDate.substring(2, 4);
        String year = facetDate.substring(4);

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    //UTC to MMDDYYYY
    public static String utcToFacetDateFormat(long utc) {
        Date date = new Date(utc);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        StringBuilder builder = new StringBuilder();

        int mon = cal.get(Calendar.MONTH) + 1;
        if (mon < 10) {
            builder.append("0");
        }
        builder.append(mon);

        int dom = cal.get(Calendar.DAY_OF_MONTH);
        if (dom < 10) {
            builder.append("0");
        }
        builder.append(cal.get(Calendar.DAY_OF_MONTH));

        builder.append(cal.get(Calendar.YEAR));

        return builder.toString();
    }

    public static long expandFromDay(long day) {
        return day * MSEC_DAY;
    }

    public static long endOfTheDay(long beginningOfTheDay) {
        return beginningOfTheDay + MSEC_DAY - 1;
    }
}
