package ru.coffeeset.cscl.reporter.controllers.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Map;

import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;
import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;

import static android.content.Context.CONNECTIVITY_SERVICE;


public final class HttpRequest {

    private Uri.Builder params;
    private int method;
    private String requestName;
    private Map<String, Object> parameters;
    private JSONObject jsonBody;


    public HttpRequest(int method, String requestName, Map<String, Object> parameters) {
        this.method = method;
        this.requestName = requestName;
        this.parameters = parameters;
    }


    public HttpRequest(int method, String requestName, Map<String, Object> parameters, JSONObject jsonBody) {
        this.method = method;
        this.requestName = requestName;
        this.parameters = parameters;
        this.jsonBody = jsonBody;
    }


    public void doRequest(RequestListener.StringCallback stringCallback) {
        final ConnectivityManager conMgr = (ConnectivityManager) ReporterApplication.getInstance()
                .getSystemService(CONNECTIVITY_SERVICE);

        if (conMgr == null) {
            stringCallback.onError(HttpError.NO_NETWORK);
            return;
        }

        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnected())) {
            //Toast.makeText(AppController.newInstance(), HttpError.NO_NETWORK.getDescription(), Toast.LENGTH_LONG).show();
            stringCallback.onError(HttpError.NO_NETWORK);
            return;
        }


        this.params = new Uri.Builder();
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                this.params.appendQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        doHttpRequest(buildUrlForRequest(requestName), stringCallback);
    }


    private String buildUrlForRequest(String requestName) {
        Uri buildUri = Uri.parse(String.format("%1$s%2$s", ReporterConfig.getBaseApiUrl(), requestName))
                .buildUpon()
                .appendEncodedPath(params.toString())
                .build();
        return Uri.decode(buildUri.toString());
    }


    private void doHttpRequest(String url, final RequestListener.StringCallback handler) {
        //Logger.d(url);

        Request request;

        switch (method) {
            case Request.Method.POST:
                request = new JsonObjectRequest(method,
                        url,
                        jsonBody,
                        response -> handler.onComplete(response.toString()),
                        error -> {
                            HttpError e = volleyErrorToHttpError(error);
                            if (e == HttpError.NO_DATA) {
                                handler.onComplete(null);
                                return;
                            }
                            Logger.e(e.getDescription());
                            handler.onError(e);
                        });
                break;
            default:
                request = new StringRequest(method,
                        url,
                        handler::onComplete,
                        error -> {
                            HttpError e = volleyErrorToHttpError(error);
                            Logger.e(e.getDescription());
                            handler.onError(e);
                        });
                break;
        }

        NetworkController.getInstance().addToRequestQueue(request, ReporterConfig.HTTP_TAG);
    }


    private HttpError volleyErrorToHttpError(VolleyError error) {
        if (error.networkResponse != null) {
            for (HttpError e : HttpError.values()) {
                if (error.networkResponse.statusCode == e.getStatusCode()) {
                    return e;
                }
            }
        } else {
            return HttpError.NO_DATA;
        }
        return HttpError.UNKNOWN_ERROR;
    }

}