package ru.coffeeset.cscl.reporter.controllers.network;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;
import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;



public class NetworkController {

    private RequestQueue requestQueue;
    private static NetworkController mInstance;


    public static synchronized NetworkController getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkController();
        }
        return mInstance;
    }


    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ReporterApplication.getInstance());
        }
        return requestQueue;
    }


    <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty

        req.setTag(TextUtils.isEmpty(tag) ? ReporterConfig.HTTP_TAG : tag);

        req.setRetryPolicy(new DefaultRetryPolicy(
                ReporterConfig.HTTP_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    <T> void addToRequestQueue(Request<T> req) {
        req.setTag(ReporterConfig.HTTP_TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
            Logger.d("HTTP requests cancelled");
        }
    }

}
