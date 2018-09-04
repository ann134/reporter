package ru.coffeeset.cscl.reporter.models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateEntry {

    private DateTime dateFrom;
    private DateTime dateToEnd;
    private DateTime dateToNow;


    public DateEntry(DateTime date) {
        dateFrom = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0, 0);
        dateToEnd = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 23, 59, 59);
        dateToNow = date;
    }

    public DateTime getDateFrom() {
        return dateFrom;
    }

    public DateTime getDateToEnd() {
        return dateToEnd;
    }

    public DateTime getDateToNow() {
        return dateToNow;
    }

    public DateTime getMonthFrom() {
        return new DateTime(dateFrom.getYear(), dateFrom.getMonthOfYear(), dateFrom.dayOfMonth().getMinimumValue(), 0, 0, 0);
    }

    public DateTime getMonthToEnd() {
        return new DateTime(dateToEnd.getYear(), dateToEnd.getMonthOfYear(), dateToEnd.dayOfMonth().getMaximumValue(), 23, 59, 59);
    }

    public DateTime getMonthToNow() {
        return dateToNow;
    }

}
