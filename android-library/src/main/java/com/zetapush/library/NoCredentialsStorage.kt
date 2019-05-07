package com.zetapush.library

/**
 * Created by damien on 24/07/17.
 */

class NoCredentialsStorage : StorageCredentialsInterface {
    override fun saveCredentials(login: String, password: String) {
        // Do nothing
    }

    override fun getCredentials(): Map<String, String>? {
        return null
    }

    override fun clearCredentials() {
        // Do nothing
    }
}
