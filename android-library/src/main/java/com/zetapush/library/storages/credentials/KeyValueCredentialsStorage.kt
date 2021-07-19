package com.zetapush.library.storages.credentials

import android.content.Context
import android.content.SharedPreferences
import com.zetapush.library.storages.EncryptedSharedPreferenceFactory

class KeyValueCredentialsStorage(
    private val context: Context,
    private val businessId: String,
    private val keyLogin: String,
    private val keyPassword: String
) : StorageCredentialsInterface {

    private object Constant {
        const val migrationKey = "isCredentialsMigratedToEncryptedSharedPref"
    }

    private val sharedPreferencesName: String = "zetapush-shared-pref-$businessId"

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferenceFactory.createSharedPref(sharedPreferencesName, context)

    override fun saveCredentials(login: String?, password: String?) {
        val editor = encryptedSharedPreferences.edit()
        login?.let { login ->
            editor.putString(keyLogin, login)
        }
        password?.let { password ->
            editor.putString(keyPassword, password)
        }
        editor.apply()
    }

    override val credentials: Credentials
        get() {
            val login = encryptedSharedPreferences.getString(keyLogin, null)
            val password = encryptedSharedPreferences.getString(keyPassword, null)

            return Credentials(login, password)
        }

    override fun clearCredentials() {
        val edit = encryptedSharedPreferences.edit()
        encryptedSharedPreferences.all.forEach { edit.remove(it.key) }
        edit.apply()
    }

    override fun migrateToSecuredStorageIfNeeded() {
        if (sharedPreferences.getBoolean(Constant.migrationKey, false)) return

        sharedPreferences.getString(keyLogin, null)?.let { login ->
            saveCredentials(login, null)
        }

        sharedPreferences.getString(keyPassword, null)?.let { password ->
            saveCredentials(null, password)
        }

        val editor = sharedPreferences.edit()
        editor.putBoolean(Constant.migrationKey, true)
        editor.apply()

    }
}
