package com.zetapush.library

/**
 * Created by damien on 20/07/17.
 */

class NoTokenStorage : StorageTokenInterface {

    override fun saveToken(token: String) {
        // Do nothing
    }

    override fun getToken(): String? {
        return null
    }

    override fun clearToken() {
        // Do nothing
    }
}
