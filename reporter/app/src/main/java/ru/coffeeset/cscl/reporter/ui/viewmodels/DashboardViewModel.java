package ru.coffeeset.cscl.reporter.ui.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.ReporterApi;
import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.NetworkController;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.repo.ReportParser;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.reports.Parameters;
import ru.coffeeset.cscl.reporter.models.reports.Report;
import ru.coffeeset.cscl.reporter.models.reports.ReportState;
import ru.coffeeset.cscl.reporter.models.reports.Revenue;


public class DashboardViewModel extends ViewModel {

    private List<Division> listFilteredDivisions;
    private DateEntry date;

    private MutableLiveData<Revenue> liveDataRevenue = new MutableLiveData<>();


    public void setListFilteredDivisions(List<Division> listFilteredDivisions) {
        this.listFilteredDivisions = listFilteredDivisions;
    }

    public void refresh() {
        cancelReQuests();

        date = new DateEntry(new DateTime());

        if (listFilteredDivisions != null) {
            loadReportPost(getParams(date.getDateFrom(), date.getDateToNow()));
            loadReportPost(getParams(date.getDateFrom().minusDays(1), date.getDateToNow().minusDays(1)));
            loadReportPost(getParams(date.getMonthFrom(), date.getMonthToNow()));
        }

    }

    private Parameters getParams(DateTime dateFrom, DateTime dateTo) {
        return new Parameters(Report.ID.revenue,
                Formatter.getDateStringISO(dateFrom),
                Formatter.getDateStringISO(dateTo),
                Parameters.Granularity.day,
                listFilteredDivisions);
    }

    private void loadReportPost(Parameters p) {

        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("parameters", p.getSerialize());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.d("json" + json);


        ReporterApi.getReport(new Revenue(), json, new RequestListener.ObjectCallback<Revenue>() {
            @Override
            public void onComplete(Revenue response) {

                //response.setState(ReportState.LOADING);

                setLiveDataRevenue(response);

            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                //если приходят три ошибки
            }
        });


        /*new ReportParser<Revenue>().getReport(new Revenue(), json, new RequestListener.ObjectCallback<Revenue>() {
            @Override
            public void onComplete(Revenue response) {

                //response.setState(ReportState.LOADING);

                setLiveDataRevenue(response);

            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                //если приходят три ошибки
            }
        });*/
    }

    private void cancelReQuests() {
        NetworkController.getInstance().cancelPendingRequests(ReporterConfig.HTTP_TAG);
    }


    public DateEntry getDate() {
        return date;
    }

    public MutableLiveData<Revenue> getLiveDataRevenue() {
        return liveDataRevenue;
    }

    private void setLiveDataRevenue(Revenue revenue) {
        liveDataRevenue.setValue(revenue);
    }

}
