package com.zetapush.library

import android.content.Context

class WeakClient : Client {

    /**
     * Get the token for the weak authentication
     *
     * @return : Token as a string
     */
    val token: String?
        get() = super.zetaPushService.token

    constructor(
        context: Context,
        businessId: String,
        deployId: String = Constants.weakDeployId,
        resource: String = Constants.resource,
        storageTokenHandler: StorageTokenInterface = KeyValueTokenStorage(context, businessId, "key_storage_token"),
        storageCredentialHandler: StorageCredentialsInterface = KeyValueCredentialsStorage(context, businessId, "key_storage_login", "key_storage_password")
    ) : super(
        businessId = businessId,
        weakDeployId = deployId,
        resource = resource,
        storageTokenHandler = storageTokenHandler,
        storageCredentialHandler = storageCredentialHandler
    )

    constructor(
        businessId: String,
        deployId: String = Constants.weakDeployId,
        resource: String = Constants.resource,
        storageTokenHandler: StorageTokenInterface,
        storageCredentialHandler: StorageCredentialsInterface
    ) : super(
        businessId = businessId,
        weakDeployId = deployId,
        resource = resource,
        storageTokenHandler = storageTokenHandler,
        storageCredentialHandler = storageCredentialHandler
    )


    /**
     * Basic Weak Authentication
     */
    fun connect() {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication()
    }
}
