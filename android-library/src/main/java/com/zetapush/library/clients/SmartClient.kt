package com.zetapush.library.clients

import android.content.Context
import android.util.Log
import com.zetapush.library.storages.credentials.Credentials
import com.zetapush.library.storages.credentials.KeyValueCredentialsStorage
import com.zetapush.library.storages.credentials.StorageCredentialsInterface
import com.zetapush.library.storages.token.KeyValueTokenStorage
import com.zetapush.library.storages.token.StorageTokenInterface

class SmartClient(
    businessId: String,
    simpleDeployId: String = Constants.simpleDeployId,
    weakDeployId: String = Constants.weakDeployId,
    resource: String = Constants.resource,
    storageTokenHandler: StorageTokenInterface,
    storageCredentialHandler: StorageCredentialsInterface,
    options: Map<String, String> = HashMap()
) : Client(businessId, simpleDeployId, weakDeployId, resource, storageTokenHandler, storageCredentialHandler, options) {

    /**
     * Get credentials (login and password)
     * @return : Map with key 'login' and 'password'
     */
    val credentials: Credentials?
        get() = zetaPushService.credentials

    /**
     * Check of the user is strongly authenticated
     * @return : true if strongly authenticated, false if not
     */
    val isStronglyAuthenticated: Boolean
        get() = this.hasCredentials() && super.isConnected

    /**
     * Check of the user is weakly authenticated
     * @return : true if weakly authenticated, false if not
     */
    val isWeaklyAuthenticated: Boolean
        get() = !this.hasCredentials() && super.isConnected

    // Constructor
    constructor(
        context: Context,
        businessId: String,
        simpleDeployId: String = Constants.simpleDeployId,
        weakDeployId: String = Constants.weakDeployId,
        resource: String = Constants.resource,
        storageTokenHandler: StorageTokenInterface = KeyValueTokenStorage(context, businessId, "key_storage_token"),
        storageCredentialHandler: StorageCredentialsInterface = KeyValueCredentialsStorage(context, businessId, "key_storage_login", "key_storage_password"),
        options: Map<String, String> = HashMap()
    ) : this(businessId, simpleDeployId, weakDeployId, resource, storageTokenHandler, storageCredentialHandler, options)

    /**
     * Basic Weak Authentication
     */
    fun connect() {
        // TODO: we need to handle reconnection for simple authent without credentials if previously we store a token !
        // TODO: maybe a fallback to a weak authent could be good for this case.
        Log.v("SmartClient", "-> ${super.canDoConnection()}")
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication()
    }

    /**
     * Basic Simple Authentication
     * @param login : Login
     * @param password : Password
     */
    fun connect(login: String, password: String) {
        Log.v("SmartClient", "-> ${super.canDoConnection()}")
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsSimpleAuthentication(login, password)
    }

    /**
     * Check if the user has credentials
     * @return : true if he has credentials, false if not
     */
    fun hasCredentials(): Boolean {
        return zetaPushService.credentials?.login != null
    }
}
