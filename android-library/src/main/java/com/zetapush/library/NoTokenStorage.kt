package com.zetapush.library

class NoTokenStorage : StorageTokenInterface {

    override fun saveToken(token: String?) {
        // Do nothing
    }

    override val token: String?
        get() = null

    override fun clearToken() {
        // Do nothing
    }
}
