package ru.coffeeset.cscl.reporter.controllers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import net.danlew.android.joda.JodaTimeAndroid;

import ru.coffeeset.cscl.reporter.controllers.network.NetworkController;
import ru.coffeeset.cscl.reporter.ui.MainActivity;


public final class ReporterApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static ReporterApplication mInstance;

    public boolean dashboardFirstRun = true;


    public static synchronized ReporterApplication getInstance() {
        if (mInstance == null) {
            mInstance = new ReporterApplication();
        }
        return mInstance;
    }


    //region -- APP LIFECYCLE CALLBACKS

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       //MultiDex.install(this);  //for support api19
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerActivityLifecycleCallbacks(this);


        JodaTimeAndroid.init(this);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity instanceof MainActivity) {
            // loadDataFromApi();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Logger.d(activity.getLocalClassName() + ": Started");
    }


    @Override
    public void onActivityResumed(Activity activity) {
        //Logger.d(activity.getLocalClassName() + ": Resumed");
    }


    @Override
    public void onActivityPaused(Activity activity) {
        //Logger.d(activity.getLocalClassName() + ": Paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //Logger.d(activity.getLocalClassName() + ": Stopped");
        if (activity instanceof MainActivity) {
              NetworkController.getInstance().cancelPendingRequests(ReporterConfig.HTTP_TAG);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        //Logger.d(activity.getLocalClassName() + ": SaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //Logger.d(activity.getLocalClassName() + ": Destroyed");
    }

    //endregion

}

