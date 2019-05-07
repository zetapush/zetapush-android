package com.zetapush.library

import android.content.Context

/**
 * Created by damien on 19/07/17.
 */

class WeakClient : Client {

    private object Constants {
        const val resource = "android"
        const val deployId = "weak_0"
    }

    /**
     * Get the token for the weak authentication
     *
     * @return : Token as a string
     */
    val token: String?
        get() = super.zetaPushService.token

    constructor(context: Context) : super(context)
    constructor(context: Context, storageTokenHandler: StorageTokenInterface) : super(context, storageTokenHandler)
    constructor(context: Context, storageCredentialHandler: StorageCredentialsInterface) : super(context, storageCredentialHandler)
    constructor(storageTokenHandler: StorageTokenInterface, storageCredentialHandler: StorageCredentialsInterface) : super(storageTokenHandler, storageCredentialHandler)

    /**
     * Basic Weak Authentication
     *
     * @param businessId : Sandbox ID
     */
    fun connect(businessId: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication(businessId, Constants.deployId, Constants.resource)
    }

    /**
     * Weak Authentication with deployment ID
     *
     * @param businessId : Sandbox ID
     * @param deployId   : Value of the authentication service
     */
    fun connect(businessId: String, deployId: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, Constants.resource)
    }

    /**
     * Weak Authentication with deployment ID and resource
     *
     * @param businessId : Sandbox ID
     * @param deployId   : Value of the authentication service
     * @param resource   : Resource
     */
    fun connect(businessId: String, deployId: String, resource: String) {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, resource)
    }
}
