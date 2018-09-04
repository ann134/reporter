package ru.coffeeset.cscl.reporter.controllers.repo;

import java.util.ArrayList;

import ru.coffeeset.cscl.reporter.models.reports.Report;


public class ReportRepository {

    private static volatile ReportRepository instance;
    private static volatile ArrayList<Report> mReportsList;
    private static volatile ArrayList<Report> mReportsList2;

    private ReportRepository() {
    }

    static synchronized ReportRepository getInstance() {
        if (instance == null) {
            instance = new ReportRepository();
            mReportsList = ReportRepository.createReportsList();
            mReportsList2 = ReportRepository.createReportsList2();
        }
        return instance;
    }


    private static ArrayList<Report> createReportsList() {
        ArrayList<Report> l = new ArrayList<>();

        l.add(new Report(Report.Position.divisions));
        l.add(new Report(Report.Position.salesMix));
        l.add(new Report(Report.Position.golden));
        l.add(new Report(Report.Position.card));

        return l;
    }

    private static ArrayList<Report> createReportsList2() {
        ArrayList<Report> l = new ArrayList<>();

        l.add(new Report(Report.Position.golden));
        l.add(new Report(Report.Position.salesMix));
        l.add(new Report(Report.Position.divisions));

        return l;
    }


    public ArrayList<Report> getReportsList() {
        return mReportsList;
    }

    public ArrayList<Report> getReportsList2() {
        return mReportsList2;
    }

}
