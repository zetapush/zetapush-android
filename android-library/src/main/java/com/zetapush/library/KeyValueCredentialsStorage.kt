package com.zetapush.library

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

/**
 * Created by damien on 20/07/17.
 */

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

    override fun getCredentials(): Map<String, String> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val login = sharedPreferences.getString(keyLogin, null)
        val password = sharedPreferences.getString(keyPassword, null)

        val credentials = HashMap<String, String>()
        login?.let { login ->
            credentials["login"] = login
        }
        password?.let { password ->
            credentials["password"] = password
        }
        return credentials
    }

    override fun clearCredentials() {
        this.saveCredentials(null, null)
    }
}
