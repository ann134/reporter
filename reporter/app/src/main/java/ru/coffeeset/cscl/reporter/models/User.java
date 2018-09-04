package ru.coffeeset.cscl.reporter.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;
import ru.coffeeset.cscl.reporter.models.accessList.AccessList;


public class User {

    private final SharedPreferences prefs;

    private static final String KEY_LOGIN = "KEY_LOGIN";
    private static final String KEY_TOKEN = "KEY_TOKEN";

    private static volatile AccessList access;



    public User(Context context) {
        prefs = context.getSharedPreferences(ReporterConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        access = new AccessList();
    }



    @Nullable
    public String getLogin() {
        return getStringValue(KEY_LOGIN);
    }

    public void setLogin(@Nullable String name) {
        setStringValue(KEY_LOGIN, name);
    }

    @Nullable
    public String getToken() {
        return getStringValue(KEY_TOKEN);
    }

    public void setToken(@Nullable String token) {
        setStringValue(KEY_TOKEN, token);
    }



    public boolean isAuthorized() {
        return !TextUtils.isEmpty(getToken());
    }

    public AccessList getAccess() {
        return access;
    }



    @SuppressLint("CommitPrefEdits")
    public void deleteUser() {
        prefs.edit()
                .remove(KEY_TOKEN)
                .apply();

        access.deleteAccessLists();
    }




    @Nullable
    private String getStringValue(String key) {
        return prefs.getString(key, null);
    }

    private void setStringValue(String key, @Nullable String value) {
        SharedPreferences.Editor editor = prefs.edit();
        if (TextUtils.isEmpty(value)) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
        editor.apply();
    }
}
