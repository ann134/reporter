package ru.coffeeset.cscl.reporter.controllers.network;


public abstract class RequestListener {

    public interface StringCallback {
        void onComplete(String response);
        void onError(HttpError e);
    }

    public interface ObjectCallback<T> {
        void onComplete(T response);
        void onError(HttpError e);
    }

}
