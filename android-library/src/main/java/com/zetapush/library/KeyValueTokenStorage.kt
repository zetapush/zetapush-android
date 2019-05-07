package com.zetapush.library

import android.content.Context
import android.preference.PreferenceManager

class KeyValueTokenStorage(
    private val context: Context,
    private val keyToken: String
) : StorageTokenInterface {

    override fun saveToken(token: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyToken, token)
        editor.apply()
    }

    override fun getToken(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(keyToken, null)
    }

    override fun clearToken() {
        this.saveToken(null)
    }
}