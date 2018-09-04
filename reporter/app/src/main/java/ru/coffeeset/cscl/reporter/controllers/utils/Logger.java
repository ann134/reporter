package ru.coffeeset.cscl.reporter.controllers.utils;

import android.util.Log;

import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;

/**
 * Created by User on 01.06.2018.
 */


public class Logger {
    private Logger() {
    }

    public static void i(Object message) {
        if (ReporterConfig.IS_DEBUG_MODE) {
            Log.i("myLog [INFO] ", getMessage(message));
        }
    }

    public static void d(Object message) {
        if (ReporterConfig.IS_DEBUG_MODE) {
            Log.d("myLog [DEBUG] ", getMessage(message));
        }
    }


    public static void e(Object message) {
        if (ReporterConfig.IS_DEBUG_MODE) {
            Log.e("myLog [ERROR] ", getMessage(message));
        }
    }

    public static void w(Object message) {
        if (ReporterConfig.IS_DEBUG_MODE) {
            Log.w("myLog [WARN] ", getMessage(message));
        }
    }

    public static void v(Object message) {
        if (ReporterConfig.IS_DEBUG_MODE) {
            Log.v("myLog [VERBOSE] ", getMessage(message));
        }
    }

    private static String getMessage(Object message) {
        return message == null ? "Message obj is NULL" : message.toString();
    }

}


