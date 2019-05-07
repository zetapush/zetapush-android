package com.zetapush.library

import android.content.Context

class SmartClient : Client {

    private object Constants {
        const val resource = "android"
        const val simpleDeployId = "simple_0"
        const val weakDeployId = "weak_0"
    }

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
    constructor(context: Context) : super(context)
    constructor(context: Context, storageTokenHandler: StorageTokenInterface) : super(context, storageTokenHandler)
    constructor(context: Context, storageCredentialHandler: StorageCredentialsInterface) : super(context, storageCredentialHandler)
    constructor(storageTokenHandler: StorageTokenInterface, storageCredentialHandler: StorageCredentialsInterface) : super(storageTokenHandler, storageCredentialHandler)

    /**
     * Basic Weak Authentication
     * @param businessId : Sandbox ID
     */
    fun connect(businessId: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication(businessId, Constants.weakDeployId, Constants.resource)
    }

    /**
     * Weak Authentication with deployment ID
     * @param businessId : Sandbox ID
     * @param deployId : Value of the authentication service
     */
    fun connect(businessId: String, deployId: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, Constants.resource)
    }

    /**
     * Basic Simple Authentication
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     */
    fun connect(businessId: String, login: String, password: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsSimpleAuthentication(
            businessId,
            login,
            password,
            Constants.simpleDeployId,
            Constants.resource
        )
    }

    /**
     * Simple Authentication with deploy ID
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     * @param deployId : Authentication service
     */
    fun connect(businessId: String, login: String, password: String, deployId: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsSimpleAuthentication(businessId, login, password, deployId, Constants.resource)
    }

    /**
     * Simple Authentication with deploy ID and resouce
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     * @param deployId : Authentication service
     * @param resource : Resource
     */
    fun connect(businessId: String, login: String, password: String, deployId: String, resource: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsSimpleAuthentication(businessId, login, password, deployId, resource)
    }

    /**
     * Check if the user has credentials
     * @return : true if he has credentials, false if not
     */
    fun hasCredentials(): Boolean {
        return zetaPushService.credentials?.login != null
    }
}
