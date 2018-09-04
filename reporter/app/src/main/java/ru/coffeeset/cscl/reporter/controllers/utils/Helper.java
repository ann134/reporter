package ru.coffeeset.cscl.reporter.controllers.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Random;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;

/**
 * Created by User on 01.06.2018.
 */


public class Helper {
    private Helper() {
    }

    //region -- ENCRYPTION

    public static class Security {
        private Security() {
        }

        /*
        Simple Base64 encryption, not strong!!!
         */
        public static String toBase64String(String input) {
            return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
        }

        public static String fromBase64String(String input) {
            return new String(Base64.decode(input, Base64.DEFAULT));
        }


    }

    //endregion

    public static class Validator {

        private Validator() {
        }

        public static boolean isValidEmail(CharSequence target) {
            return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

        //for old api compatibility
        public static boolean objectEquals(Object o1, Object o2) {
            return o1 == null && o2 == null || o1 != null && o1.equals(o2);
        }
    }


    //region -- DATE FORMATTER

    public static class Formatter {
        private Formatter() {
        }

        public static String dateTimeToIsoString(DateTime dateTime) {
            DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
            return dateTime.toString(f);
        }

        public static String dateTimeToShortDateString(DateTime dateTime) {
            DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd");
            return dateTime.toString(f);
        }

        public static String dateTimeToDateTimeString(DateTime dateTime) {
            DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            return dateTime.toString(f);
        }

        public static String dateTimeToReadableDateTimeString(DateTime date) {
            return date.toString(DateTimeFormat.mediumDateTime());
        }

        public static String dateTimeToReadableDateString(DateTime date) {
            return date.toString(DateTimeFormat.mediumDate());
        }

        public static String decimalToFormattedDecimalString(Double value, int decimals) {
            String format = "% .".concat(String.valueOf(decimals)).concat("f");
            return String.format(format, value);
        }


        public static String getCurrencyIcon(String currency) {
            if (TextUtils.isEmpty(currency)) {
                return "";
            }

            switch (currency.toLowerCase()) {
                case "rub":
                    return Utils.getString(R.string.fa_rub);
                case "eur":
                    return Utils.getString(R.string.fa_eur);
                case "usd":
                    return Utils.getString(R.string.fa_usd);
                default:
                    return currency;
            }
        }

    }

//endregion


    //region -- UTILS

    public static class Utils {
        private Utils() {
        }


        public static Locale getCurrentLocale() {
            Locale locale;
            Context context = ReporterApplication.getInstance().getApplicationContext();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                locale = context.getResources().getConfiguration().locale;
            }
            return locale;
        }

        public static String getRandomString(int length, Boolean numbersOnly) {
            String characters = numbersOnly ? "0123456789" : "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            char[] text = new char[length];
            for (int i = 0; i < length; i++) {
                text[i] = characters.charAt(random.nextInt(characters.length()));
            }
            return new String(text);
        }

        public static void hideKeyboard(View view) {
            InputMethodManager inputMethodManager = (InputMethodManager) ReporterApplication.getInstance().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }


        public static int getColor(int id) {
            Context context = ReporterApplication.getInstance();
            return ContextCompat.getColor(context, id);
        }

        public static Drawable getDrawable(int id) {
            Context context = ReporterApplication.getInstance().getApplicationContext();
            return AppCompatResources.getDrawable(context, id);

        }

        public static String getString(int id) {
            Context context = ReporterApplication.getInstance().getApplicationContext();
            return context.getString(id);
        }


    }


//endregion



}
