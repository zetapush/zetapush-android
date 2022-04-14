package com.zetapush.library.storages

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.File

object EncryptedSharedPreferenceFactory {
    fun createSharedPref(fileName: String, context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return tryCreateSharedPreferences(context, fileName, masterKey, true)
    }

    private fun tryCreateSharedPreferences(
        context: Context,
        fileName: String,
        masterKey: MasterKey,
        retryOnce: Boolean
    ): SharedPreferences {
        try {
            return EncryptedSharedPreferences.create(
                context,
                fileName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // If encrypted data becomes corrupted, EncryptedSharedPreferences library is not yet able to sort out the issue on its own
            // Depending on the corruption type, various exceptions may be thrown(Crypto, ProtoBuff, CharException...)
            // So we catch any exception, trash sharedPrefs File and retry Once only
            if (retryOnce) {
                deleteSharedPreferences(context, fileName)
                return tryCreateSharedPreferences(context, fileName, masterKey, false)
            } else {
                throw e
            }
        }
    }

    private fun deleteSharedPreferences(context: Context, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.deleteSharedPreferences(name)
        } else {
            val dir = File(context.applicationInfo.dataDir, "shared_prefs")
            File(dir, "$name.xml").delete()
        }
    }
}
