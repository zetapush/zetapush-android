package com.zetapush.testsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by damien on 20/07/17.
 */

public class KeyValueCredentialsStorage implements StorageCredentialsInterface {

    // Variables
    private final Context context;
    private final String keyLogin;
    private final String keyPassword;

    // Constructor
    public KeyValueCredentialsStorage(Context context, String keyLogin, String keyPassword) {
        this.context = context;
        this.keyLogin = keyLogin;
        this.keyPassword = keyPassword;
    }


    @Override
    public void saveCredentials(String login, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyLogin, login);
        editor.putString(keyPassword, password);
        editor.commit();
    }

    @Override
    public Map<String, String> getCredentials() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String login = sharedPreferences.getString(keyLogin, null);
        String password = sharedPreferences.getString(keyPassword, null);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);
        return credentials;
    }

    @Override
    public void clearCredentials() {
        this.saveCredentials(null, null);
    }
}
