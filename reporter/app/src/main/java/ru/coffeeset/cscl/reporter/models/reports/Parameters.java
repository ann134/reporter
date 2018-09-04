package ru.coffeeset.cscl.reporter.models.reports;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.models.accessList.Division;

import static java.lang.String.valueOf;


public class Parameters {

    private Security security;
    private Parameters.Report report;


    public static class Granularity {
        public static String day = "day";
        public static String month = "month";
        public static String year = "year";
    }


    private class Security {

        private String token;
        private String reportId;

    }


    private class Report {

        private String dateFrom;
        private String dateTo;
        private String granularity;
        private List<String> refDivision;

    }


    public Parameters(int id, String dateFrom, String dateTo, String granularity, List<Division> divisions) {
        security = new Security();
        report = new Report();

        security.reportId = valueOf(id);
        security.token = MainRepository.users().getUser().getToken();

        report.dateFrom = dateFrom;
        report.dateTo = dateTo;
        report.granularity = granularity;

        report.refDivision = new ArrayList<>();
        for (Division d : divisions)
            report.refDivision.add(d.getRef());
    }

    public JSONObject getSerialize() throws JSONException {
        return new JSONObject(new Gson().toJson(this));
    }

}
