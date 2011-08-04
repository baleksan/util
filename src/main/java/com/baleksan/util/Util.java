package com.baleksan.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class Util {
    public static final String EOL = System.getProperty("line.separator");
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static String list2Str(List stringList) {
        StringBuilder builder = new StringBuilder();
        for (Object str : stringList) {
            builder.append(str.toString());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public static String set2Str(Set stringList) {
        StringBuilder builder = new StringBuilder();
        for (Object str : stringList) {
            builder.append(str.toString());
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public static String array2Str(Object[] objectArray) {
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
    public static String formatStackTrace(Exception ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMessage() == null ? "no message" : ex.getMessage());
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

}
