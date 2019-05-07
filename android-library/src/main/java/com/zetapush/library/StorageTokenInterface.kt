package com.zetapush.library

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


}
