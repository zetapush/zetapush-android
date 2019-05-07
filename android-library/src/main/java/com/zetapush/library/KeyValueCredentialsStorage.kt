package com.zetapush.library

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class KeyValueCredentialsStorage(
    private val context: Context,
    private val businessId: String,
    private val keyLogin: String,
    private val keyPassword: String
) : StorageCredentialsInterface {

    private val sharedPreferencesName: String = "zetapush-shared-pref-$businessId"

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    override fun saveCredentials(login: String?, password: String?) {
        val editor = sharedPreferences.edit()
        login?.let { login ->
            editor.putString(keyLogin, login)
        }
        password?.let { password ->
            editor.putString(keyPassword, password)
        }
        editor.apply()
    }

    override val credentials: Credentials?
        get() {
            val login = sharedPreferences.getString(keyLogin, null)
            val password = sharedPreferences.getString(keyPassword, null)

            return Credentials(login, password)
        }

    override fun clearCredentials() {
        val edit = sharedPreferences.edit()
        sharedPreferences.all.forEach { edit.remove(it.key) }
        edit.apply()
    }
}
