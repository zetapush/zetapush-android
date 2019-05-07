package com.zetapush.library

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

class KeyValueCredentialsStorage(
    private val context: Context,
    private val keyLogin: String,
    private val keyPassword: String
) : StorageCredentialsInterface {

    override fun saveCredentials(login: String?, password: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyLogin, login)
        editor.putString(keyPassword, password)
        editor.apply()
    }

    override val credentials: Credentials?
        get() {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val login = sharedPreferences.getString(keyLogin, null)
            val password = sharedPreferences.getString(keyPassword, null)

            return Credentials(login, password)
        }

    override fun clearCredentials() {
        this.saveCredentials(null, null)
    }
}
