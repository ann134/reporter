package ru.coffeeset.cscl.reporter.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import org.joda.time.DateTime;

import java.util.Locale;

import ru.coffeeset.cscl.reporter.BuildConfig;


public final class ReporterConfig {

    //region -- GLOBAL CONSTANTS

    public static final Boolean IS_DEBUG_MODE = true;   //enable logging and dev controllers
    public static final Boolean IS_TEST_MODE = true;    //use test or production server API URLs

    public static final String APP_ID = "android_Reporter";
    public static final String APP_NAME = "CSCL Reporter";
    private static final int APP_BUILD = BuildConfig.VERSION_CODE;
    private static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String CARD_PREFIX = "589=238=";
    public static final String SHARED_PREFERENCES_NAME = "MCC_PREFS";


    public static String getCopyrightString() {
        return "© " + new DateTime().year().getAsText() + " Coffeeshop Company. ver. " + getAppVersionString();
    }

    @SuppressLint("HardwareIds")
    public static synchronized String getAndroidId(Context context) {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ignored) {
        }
        return androidId;
    }

    public static String getAppVersionString() {
        String prefix = IS_TEST_MODE ? "test " : "";
        prefix += IS_DEBUG_MODE ? " dev " : "";
        return prefix.concat(APP_VERSION).concat(String.format(Locale.getDefault(), "(%d)", APP_BUILD));
    }

    //endregion


    //region -- NETWORK SETTINGS

    public static String getBaseApiUrl() {
        return IS_TEST_MODE ? "https://cscl.coffeeset.ru/ws-test/mobile/reporter"
                : "https://cscl.coffeeset.ru/ws/mobile/reporter";
    }

    public static final String HTTP_TAG = "CSCL_HTTPS_REQUEST";
    public static final int HTTP_REQUEST_TIMEOUT = 20 * 1000; //in milliseconds
    public static final int CARDS_REFRESH_INTERVAL = IS_DEBUG_MODE ? 60 * 1000 : 30 * 1000; //in milliseconds

    //endregion

    //public  static final String developerName = "Anna";
}
