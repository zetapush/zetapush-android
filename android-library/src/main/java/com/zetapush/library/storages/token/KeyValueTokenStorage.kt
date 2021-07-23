package com.zetapush.library.storages.token

import android.content.Context
import android.content.SharedPreferences
import com.zetapush.library.storages.EncryptedSharedPreferenceFactory
import com.zetapush.library.storages.credentials.KeyValueCredentialsStorage

class KeyValueTokenStorage(
    private val context: Context,
    private val businessId: String,
    private val keyToken: String
) : StorageTokenInterface {

    private object Constant {
        const val migrationKey = "isTokenMigratedToEncryptedSharedPref"
    }

    private val sharedPreferencesName: String = "zetapush-shared-pref-$businessId"

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferenceFactory.createSharedPref(sharedPreferencesName, context)

    override fun saveToken(token: String?) {
        token?.let {
            val editor = encryptedSharedPreferences.edit()
            editor.putString(keyToken, it)
            editor.apply()
        }
    }

    override val token: String?
        get() = encryptedSharedPreferences.getString(keyToken, null)

    override fun clearToken() {
        val edit = encryptedSharedPreferences.edit()
        encryptedSharedPreferences.all.forEach { edit.remove(it.key) }
        edit.apply()
    }

    override fun migrateToSecuredStorageIfNeeded() {
        if (sharedPreferences.getBoolean(Constant.migrationKey, false)) return

        val token = sharedPreferences.getString(keyToken, null)
        saveToken(token)

        val editor = sharedPreferences.edit()
        editor.putBoolean(Constant.migrationKey, true)
        editor.apply()
    }
}