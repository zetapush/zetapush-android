package com.zetapush.library

/**
 * Created by damien on 20/07/17.
 */

interface StorageCredentialsInterface {

    /**
     * Get the credentials as a map with key 'login' and 'password'
     *
     * @return
     */
    val credentials: Map<String, String>?

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
}
