package ru.coffeeset.cscl.reporter.controllers.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Formatter {

    public static String getDateStringISO(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss");
        return f.print(date);
    }


    public static String getDateStringShortISO(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("YYYY-MM-dd");
        return f.print(date);
    }

    public static String getDateStringWithMonth(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("dd MMM YYYY");
        return f.print(date);
    }


    public static String getDateStringShortWithMonth(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("dd MMM");
        return f.print(date);
    }

    public static String getDateStringUsual(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("dd.MM.YYYY");
        return f.print(date);
    }

    public static String getDateStringTime(DateTime date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("HH:mm:ss");
        return f.print(date);
    }


    public static String formatInt(double i) {
        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(3);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(dfs);

        return df.format((int) i);
    }
}
