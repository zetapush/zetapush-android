package com.zetapush.library.storages.credentials

class NoCredentialsStorage : StorageCredentialsInterface {
    override fun saveCredentials(login: String?, password: String?) {
        // Do nothing
    }

    override val credentials: Credentials?
        get() = null

    override fun clearCredentials() {
        // Do nothing
    }

    override fun migrateToSecuredStorageIfNeeded() {
        // Do nothing
    }
}
