package com.baleksan.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class Util
{
    public static final String EOL = System.getProperty("line.separator");
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static final Map<Integer, String> encodingSeparators;

    static {
        encodingSeparators = new HashMap<Integer, String>();
        encodingSeparators.put(0, ";");
        encodingSeparators.put(1, ":");
        encodingSeparators.put(2, "/");
    }


    public static String list2Str(List stringList)
    {
        StringBuilder builder = new StringBuilder();
        for (Object str : stringList) {
            builder.append(str.toString());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public static String set2Str(Set stringList)
    {
        StringBuilder builder = new StringBuilder();
        for (Object str : stringList) {
            builder.append(str.toString());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public static String array2Str(Object[] objectArray)
    {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objectArray) {
            builder.append(obj.toString());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    /**
     * Formats stack trace to print the exception stack trace (to be used
     * for instance in the Hadoop jobs)
     *
     * @param ex
     * @return string which represents the stack trace
     */
    public static String formatStackTrace(Exception ex)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage());
        builder.append(EOL);
        StackTraceElement[] trace = ex.getStackTrace();
        int index = 0;
        for (StackTraceElement element : ex.getStackTrace()) {
            builder.append(element.toString());

            if (index + 1 < trace.length) {
                builder.append(EOL);
            }
        }
        return builder.toString();
    }


    /**
     * Normalizes name for use in string encoding
     *
     * @param str
     * @param list of separators to normalize out
     * @return
     */
    public static String normalizeName(String str, String... separators)
    {
        for (String separator : separators) {
            str = str.replaceAll(separator, "_");
        }

        return str;
    }

    /**
     * Normalizes name for use in string encoding
     *
     * @param str
     * @param list of separators to normalize out
     * @return
     */
    public static String normalizeName(String str)
    {
        for (String separator : encodingSeparators.values()) {
            str = str.replaceAll(separator, "_");
        }

        return str;
    }

    public static String getSeparator(int level)
    {
        return encodingSeparators.get(level);
    }

}
