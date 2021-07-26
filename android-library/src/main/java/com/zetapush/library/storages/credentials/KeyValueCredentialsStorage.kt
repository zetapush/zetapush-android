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

        if(!login.isNullOrBlank() || !password.isNullOrBlank()) {
            editor.putString(keyLogin, login)
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
        edit.remove(keyLogin)
        edit.remove(keyPassword)
        edit.apply()
    }

    override fun migrateToSecuredStorageIfNeeded() {
        if (sharedPreferences.getBoolean(Constant.migrationKey, false)) return

        val login = sharedPreferences.getString(keyLogin, null)
        val password = sharedPreferences.getString(keyPassword, null)
        saveCredentials(login, password)

        clearAllOldCredentials()

        val editor = sharedPreferences.edit()
        editor.putBoolean(Constant.migrationKey, true)
        editor.apply()
    }

    private fun clearAllOldCredentials() {
        val edit = sharedPreferences.edit()
        edit.remove(keyLogin)
        edit.remove(keyPassword)
        edit.apply()
    }
}
