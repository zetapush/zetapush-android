package com.zetapush.library

import android.content.Context
import android.content.SharedPreferences

class KeyValueTokenStorage(
    private val context: Context,
    private val businessId: String,
    private val keyToken: String
) : StorageTokenInterface {

    private val sharedPreferencesName: String = "zetapush-shared-pref-$businessId"

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    override fun saveToken(token: String?) {
        val editor = sharedPreferences.edit()
        token?.let { token ->
            editor.putString(keyToken, token)
        }
        editor.apply()
    }

    override val token: String?
        get() = sharedPreferences.getString(keyToken, null)

    override fun clearToken() {
        val edit = sharedPreferences.edit()
        sharedPreferences.all.forEach { edit.remove(it.key) }
        edit.apply()
    }
}