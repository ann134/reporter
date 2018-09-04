package ru.coffeeset.cscl.reporter.ui.viewmodels;

import android.arch.lifecycle.ViewModel;

import org.json.JSONObject;

import ru.coffeeset.cscl.reporter.controllers.ReporterApi;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.repo.ReportParser;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.reports.IReport;

public class ReportsViewModel extends ViewModel {


    public <T extends IReport> void getReportPost(T rep, JSONObject jsonBody, RequestListener.ObjectCallback<T> handler) {
        RequestListener.ObjectCallback<T> listener = new RequestListener.ObjectCallback<T>() {

            @Override
            public void onComplete(T response) {
                handler.onComplete(response);
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                handler.onError(e);
            }
        };

        //надо обращаться к апи
        ReporterApi.getReport(rep, jsonBody, listener);
        //new ReportParser<T>().getReport(rep, jsonBody, listener);
    }
}
