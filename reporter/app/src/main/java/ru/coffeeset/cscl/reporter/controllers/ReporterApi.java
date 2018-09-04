package ru.coffeeset.cscl.reporter.controllers;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.HttpRequest;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.repo.ReportParser;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.reports.IReport;

public class ReporterApi {

    private ReporterApi() {
    }

    public static class ApiPath {
        static final String LOGIN = "/login";
        //static final String REPORT = "/report";
        static final String REPORTREADER = "/reportReader";
    }


//getLogin

    public static void logIn(String login, String password, final RequestListener.StringCallback stringCallback) {

        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        processLogin(stringCallback, params);
    }

    public static void logIn(String token, final RequestListener.StringCallback stringCallback) {

        Map<String, Object> params = new HashMap<>();
        params.put("token", token);

        processLogin(stringCallback, params);
    }


    private static void processLogin(RequestListener.StringCallback stringCallback, Map<String, Object> params) {
        new HttpRequest(Request.Method.GET, ApiPath.LOGIN, params).doRequest(new RequestListener.StringCallback() {
            @Override
            public void onComplete(String response) {
                Logger.v("api " + response);
                stringCallback.onComplete(response);
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("api Login requestPermission error: " + e.getDescription());
                stringCallback.onError(e);
            }
        });
    }

//getReport

    public static <T extends IReport> void getReport(T reportType, JSONObject jsonBody, RequestListener.ObjectCallback<T> handler) {
        RequestListener.StringCallback listener = new RequestListener.StringCallback() {
            @Override
            public void onComplete(String response) {
                Logger.d("api " + response);

                new ReportParser<>(reportType, response, callback ->
                    handler.onComplete(callback)
                ).execute();
            }
            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                handler.onError(e);
            }
        };

        new HttpRequest(Request.Method.POST, ApiPath.REPORTREADER, null, jsonBody).doRequest(listener);
    }

}
