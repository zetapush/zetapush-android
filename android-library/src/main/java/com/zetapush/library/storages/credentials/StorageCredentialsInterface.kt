package com.zetapush.library.storages.credentials

import com.zetapush.library.storages.credentials.Credentials

interface StorageCredentialsInterface {

    /**
     * Get the credentials as a map with key 'login' and 'password'
     *
     * @return
     */
    val credentials: Credentials?

    /**
     * Save the credentials used for the simple authentication
     *
     * @param login
     * @param password
     */
    fun saveCredentials(login: String?, password: String?)

    /**
     * Clear the saved credentials
     */
    fun clearCredentials()

    /**
     * Migrate credentials to a secured storage if needed. Remove this method after 01/01/2022
     */
    @Deprecated(message = "Remove this method after 01/01/2022")
    fun migrateToSecuredStorageIfNeeded()
}
