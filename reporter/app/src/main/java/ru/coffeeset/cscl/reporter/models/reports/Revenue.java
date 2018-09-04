package ru.coffeeset.cscl.reporter.models.reports;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.models.accessList.Division;


public class Revenue implements IReport {


//PARSING NEW REPORT

    public Revenue() {
    }

    public Revenue(String report) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ReportItem.class, new RevenueDeserializer())
                .create();

        Type type = new TypeToken<List<ReportItem>>() {
        }.getType();

        ReportItem = new ArrayList<>(gson.fromJson(report, type));
    }

    public class RevenueDeserializer implements JsonDeserializer<ReportItem> {
        @Override
        public ReportItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ReportItem reportItem = new ReportItem();
            JsonObject jsonObject = json.getAsJsonObject();

            reportItem.setDivision((Division) MainRepository.users().getUser().getAccess().findByRef(jsonObject.get("RefDivision").getAsString()));
            reportItem.setRevenue(jsonObject.get("Revenue").getAsDouble());
            reportItem.setPlanRevenue(jsonObject.get("PlanRevenue").getAsDouble());
            reportItem.setReportDate(jsonObject.get("ReportDate").getAsString());

            return reportItem;
        }
    }


// REVENUE

    private String state = ReportState.LOADING;
    private List<ReportItem> ReportItem;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ReportItem> getReportItems() {
        return ReportItem;
    }

    public String getReportDate() {
        return ReportItem.get(0).getReportDate();
    }


// GET TOTAL INFO REPORTS ITEMS

    //string
    public String getTotalFact() {
        return Formatter.formatInt(getSumRevenue());
    }

    public String getTotalProgress() {
        return String.format("%1$d%%", (int) getProgress());
    }

    public String getTotalFactByProperty(int property) {
        return Formatter.formatInt(getSumRevenueByProperty(property));
    }

    public String getTotalPlanByProperty(int property) {
        return Formatter.formatInt(getSumPlanRevenueByProperty(property));
    }


    //double
    public double getSumRevenue() {
        if (ReportItem == null)
            return 12;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return ReportItem.stream().mapToDouble(reportItem -> reportItem.Revenue).sum();
        } else {
            double sum = 0;
            for (ReportItem i : ReportItem) {
                sum = sum + i.getRevenue();
            }
            return sum;
        }
    }

    public double getSumPlanRevenue() {
        if (ReportItem == null)
            return 12;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return ReportItem.stream().mapToDouble(reportItem -> reportItem.PlanRevenue).sum();
        } else {
            double sum = 0;
            for (ReportItem i : ReportItem) {
                sum = sum + i.getPlanRevenue();
            }
            return sum;
        }
    }

    public double getSumRevenueByProperty(int property) {
        if (ReportItem == null)
            return 12;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return ReportItem.stream().filter(reportItem -> reportItem.division.isProperty(property)).mapToDouble(reportItem -> reportItem.Revenue).sum();
        } else {
            double sum = 0;
            for (ReportItem i : ReportItem) {
                if (i.getDivision().isProperty(property))
                    sum = sum + i.getRevenue();
            }
            return sum;
        }
    }

    public double getSumPlanRevenueByProperty(int property) {
        if (ReportItem == null)
            return 12;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return ReportItem.stream().filter(reportItem -> reportItem.division.isProperty(property)).mapToDouble(reportItem -> reportItem.PlanRevenue).sum();
        } else {
            double sum = 0;
            for (ReportItem i : ReportItem) {
                if (i.getDivision().isProperty(property))
                    sum = sum + i.getPlanRevenue();
            }
            return sum;
        }
    }

    public double getProgress() {
        if (ReportItem == null)
            return 12;

        double revenueFact = getSumRevenue();
        double revenuePlan = getSumPlanRevenue();
        double progress = revenueFact != 0 && revenuePlan != 0 ? (revenueFact * 100) / revenuePlan : 0;

        return progress;
    }


// REPORT ITEM

    public class ReportItem {

        private Division division;
        private double Revenue;
        private double PlanRevenue;
        private String ReportDate;

        public void setDivision(Division division) {
            this.division = division;
        }

        public void setRevenue(double revenue) {
            Revenue = revenue;
        }

        public void setPlanRevenue(double planRevenue) {
            PlanRevenue = planRevenue;
        }

        public void setReportDate(String reportDate) {
            ReportDate = reportDate;
        }


        public Division getDivision() {
            return division;
        }

        private double getRevenue() {
            return Revenue;
        }

        private double getPlanRevenue() {
            return PlanRevenue;
        }

        private String getReportDate() {
            return ReportDate;
        }


        public String getProgressString() {
            return String.format("%1$d%%", (int) getProgress());
        }

        public double getProgress() {
            return Revenue != 0 && PlanRevenue != 0 ? (Revenue * 100) / PlanRevenue : 0;
        }
    }

}
