package ru.coffeeset.cscl.reporter.controllers.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;


public class ReporterPermissionProvider {

    public static final int LOCALE_PERMISSION = 0;


    public static boolean hasPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermission(Activity activity, String permission, final int code) {


            /*String askForPermissionText;
            switch (permission) {
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    askForPermissionText = "For find the nearest Coffeeshop";
                    break;
                case Manifest.permission.RECEIVE_SMS:
                    askForPermissionText = "For receive the SMS with a confirmation code";
                    break;
                default:
                    askForPermissionText = "";
                    break;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the users's response! After the users
                // sees the explanation, try again to requestPermission the permission.
                if (!TextUtils.isEmpty(askForPermissionText)) {
                    Toast.makeText(activity, askForPermissionText, Toast.LENGTH_LONG).show();
                }
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            } else {
                // No explanation needed, we can requestPermission the permission.
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            }*/
    }


}
