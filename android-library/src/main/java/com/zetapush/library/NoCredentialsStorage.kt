package com.zetapush.library

class NoCredentialsStorage : StorageCredentialsInterface {
    override fun saveCredentials(login: String?, password: String?) {
        // Do nothing
    }

    override val credentials: Credentials?
        get() = null

    override fun clearCredentials() {
        // Do nothing
    }
}
