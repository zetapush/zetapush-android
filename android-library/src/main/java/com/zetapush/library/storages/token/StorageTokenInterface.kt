package com.zetapush.library.storages.token

interface StorageTokenInterface {

    /**
     * Get the token as a string
     *
     * @return : token
     */
    val token: String?

    /**
     * Save the token used for the Weak Authentication
     *
     * @param token : Token as a string
     */
    fun saveToken(token: String?)

    /**
     * Clear the saved token
     */
    fun clearToken()

    /**
    * Migrate tokens to a secured storage if needed. Remove this method after 01/01/2022
    */
    @Deprecated(message = "Remove this method after 01/01/2022")
    fun migrateToSecuredStorageIfNeeded()
}
