package ru.coffeeset.cscl.reporter.controllers.repo;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.reports.IReport;


public class ReportParser<T extends IReport> extends AsyncTask<Void, T, T> {

    public interface AsyncTaskCallback<T> {
        void onResult(T report);
    }

    private T reportType;
    private String result;
    private AsyncTaskCallback<T> callback;

    public ReportParser(T reportType, String result, AsyncTaskCallback<T> callback) {
        this.reportType = reportType;
        this.result = result;
        this.callback = callback;
    }

    @Override
    protected T doInBackground(Void... voids) {
        try {
            Logger.d("parsing");

            JSONObject json = new JSONObject(result);
            String report = json.getString("Report");

            JSONObject json2 = new JSONObject(report);
            String report2 = json2.getJSONArray("ReportItem").toString();

            Constructor constructor = reportType.getClass().getConstructor(String.class);
            return (T) constructor.newInstance(report2);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Logger.d("PARSING REPORT RETURN NULL");
        return null;

    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        callback.onResult(result);
    }
}





