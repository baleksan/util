package com.baleksan.util.compression;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class DateReducerConverterTest {

    @Test
    public void testFacetDateEncoding() {
        //MMDDYYYY
        long utc = DateReducerConverter.toFacetDateFormat2UTC("09212010");
        Assert.assertEquals(utc, 1285052400000L);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(utc);
        int year = cal.get(Calendar.YEAR);
        Assert.assertEquals(year, 2010);
        int month = cal.get(Calendar.MONTH);
        Assert.assertEquals(month, 8);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Assert.assertEquals(day, 21);

        String format = DateFormat.getInstance().format(new java.util.Date(utc));
        Assert.assertEquals(format, "9/21/10 12:00 AM");
    }

    @Test
    public void testDateToFacetEncoding() {
        //MMDDYYYY
        String format = DateReducerConverter.utcToFacetDateFormat(1285052400000L);
        Assert.assertEquals(format, "09212010");

        format = DateReducerConverter.utcToFacetDateFormat(1292918400000L);
        Assert.assertEquals(format, "12212010");

        format = DateReducerConverter.utcToFacetDateFormat(1293002890008L);
        Assert.assertEquals(format, "12212010");
    }

    @Test
    public void testEndOfTheDay() {
        //MMDDYYYY
        String format = DateReducerConverter.utcToFacetDateFormat(1285052400000L);
        Assert.assertEquals(format, "09212010");

        format = DateReducerConverter.utcToFacetDateFormat(DateReducerConverter.endOfTheDay(1285052400000L));
        Assert.assertEquals(format, "09212010");

        format = DateReducerConverter.utcToFacetDateFormat(DateReducerConverter.endOfTheDay(1285052400000L + 1));
        Assert.assertEquals(format, "09222010");
    }
}
