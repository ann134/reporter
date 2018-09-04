package ru.coffeeset.cscl.reporter.controllers.repo;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.ReporterApi;
import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.User;
import ru.coffeeset.cscl.reporter.models.accessList.Currency;

public class UserRepository {

    private static volatile UserRepository instance;
    private static volatile User mUser;

    private UserRepository() {}


    static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
            mUser = new User(ReporterApplication.getInstance().getApplicationContext());
        }
        return instance;
    }


    public User getUser() {
        return mUser;
    }

    public void deleteUser() {
        mUser.deleteUser();
    }

    private void saveUser(String login, String token) {
        mUser.setLogin(login);
        mUser.setToken(token);
    }


    private void createAccessList(String list) throws JsonParseException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Currency>>() {
        }.getType();

        mUser.getAccess().createElementsLists(new ArrayList<>(gson.fromJson(list, type)));
    }




    public void logIn(String token, final RequestListener.ObjectCallback<Boolean> handler) {
        RequestListener.StringCallback listener = new RequestListener.StringCallback() {
            @Override
            public void onComplete(String response) {
                handler.onComplete(parse(mUser.getLogin(), response));
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                handler.onError(e);
                // mUser.deleteUser();?

            }
        };

        ReporterApi.logIn(token, listener);
    }

    public void logIn(String login, String password, final RequestListener.ObjectCallback<Boolean> handler) {

        RequestListener.StringCallback listener = new RequestListener.StringCallback() {
            @Override
            public void onComplete(String response) {
                handler.onComplete(parse(login, response));
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                handler.onError(e);
                mUser.deleteUser();
            }
        };

        ReporterApi.logIn(login, password, listener);
    }


    private boolean parse(String login, String response) {
        try {
            JSONObject json = new JSONObject(response);

            String token = json.getString("tokenString");
            saveUser(login, token);

            JSONObject ac = json.getJSONObject("accessList");
            String cur = ac.getJSONArray("currencyList").toString();
            createAccessList(cur);

            return true;
        } catch (JSONException e) {
            Logger.e("Error parse users: " + e.getLocalizedMessage());
            mUser.deleteUser();
            return false;
        }
    }

}
