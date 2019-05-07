package com.zetapush.library

data class Credentials(val login: String? = null, val password: String? = null)

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
}
