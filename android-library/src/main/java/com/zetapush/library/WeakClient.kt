package com.zetapush.library

import android.content.Context

class WeakClient(
    businessId: String,
    deployId: String = Constants.weakDeployId,
    resource: String = Constants.resource,
    storageTokenHandler: StorageTokenInterface,
    storageCredentialHandler: StorageCredentialsInterface,
    options: Map<String, String> = HashMap()
) : Client(
    businessId = businessId,
    weakDeployId = deployId,
    resource = resource,
    storageTokenHandler = storageTokenHandler,
    storageCredentialHandler = storageCredentialHandler,
    options = options
) {

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
        storageCredentialHandler: StorageCredentialsInterface = KeyValueCredentialsStorage(context, businessId, "key_storage_login", "key_storage_password"),
        options: Map<String, String> = HashMap()
    ) : this(
        businessId,
        deployId,
        resource,
        storageTokenHandler,
        storageCredentialHandler,
        options
    )

    /**
     * Basic Weak Authentication
     */
    fun connect() {
        if (!super.canDoConnection()) return
        super.zetaPushService.connectionAsWeakAuthentication()
    }
}
