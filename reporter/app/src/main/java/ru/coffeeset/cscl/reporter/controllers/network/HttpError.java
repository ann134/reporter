package ru.coffeeset.cscl.reporter.controllers.network;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;


public enum HttpError {

    UNKNOWN_ERROR(0),

    NO_NETWORK(1),

    NO_DATA(2),

    BAD_REQUEST(400),

    UNAUTHORIZED(401),

    FORBIDDEN(403),

    NOT_FOUND(404),

    LOGIN_ERROR(1404),

    SERVER_ERROR(500),

    SERVER_UNAVAILABLE(503),

    ALREADY_EXISTS(409);

    private int statusCode;

    HttpError(final int statusCode) {
        this.statusCode = statusCode;
    }


    public int getStatusCode() {
        return this.statusCode;
    }


    public String getDescription() {
        switch (this) {
            case NO_NETWORK:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_NO_NETWORK);
            case NO_DATA:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_NO_DATA);
            case BAD_REQUEST:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_REQUEST_ERROR);
            case UNAUTHORIZED:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_UNAUTHORIZED);
            case FORBIDDEN:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_FORBIDDEN);
            case NOT_FOUND:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_NOT_FOUND);
            case LOGIN_ERROR:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_LOGIN_ERROR);
            case SERVER_ERROR:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_SERVER_ERROR);
            case SERVER_UNAVAILABLE:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_SERVICE_UNAVAILABLE);
            case ALREADY_EXISTS:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_ALREADY_EXISTS);
            default:
                return ReporterApplication.getInstance().getResources().getString(R.string.ERROR_DESC_UNKNOWN);
        }
    }
}