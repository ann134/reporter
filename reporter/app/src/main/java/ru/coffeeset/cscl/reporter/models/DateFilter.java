package ru.coffeeset.cscl.reporter.models;

import org.joda.time.DateTime;

public class DateFilter {

    private DateEntry dateEntry;


    public void setDate(DateEntry dateEntry){
        this.dateEntry = dateEntry;
    }

    public DateEntry getDate() {
        return dateEntry == null ? new DateEntry(new DateTime()) : dateEntry;
    }
}
