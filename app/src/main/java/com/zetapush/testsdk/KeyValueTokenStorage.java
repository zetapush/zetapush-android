package com.zetapush.testsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KeyValueTokenStorage implements StorageTokenInterface {

    // Variable
    private final Context context;
    private final String keyToken;

    // Constructor
    public KeyValueTokenStorage(Context context, String keyToken) {
        this.context = context;
        this.keyToken = keyToken;
    }

    @Override
    public void saveToken(String token) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyToken, token);
        editor.commit();
    }

    @Override
    public String getToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(keyToken, null);
    }

    @Override
    public void clearToken() {
        this.saveToken(null);
    }
}