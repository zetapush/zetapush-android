package com.zetapush.library

import android.content.Context
import com.zetapush.client.ConnectionStatusListener
import com.zetapush.client.highlevel.ZetapushClient

open class Client(
    businessId: String,
    simpleDeployId: String = Constants.weakDeployId,
    weakDeployId: String = Constants.weakDeployId,
    resource: String,
    storageTokenHandler: StorageTokenInterface,
    storageCredentialHandler: StorageCredentialsInterface,
    options: Map<String, String>
) {

    protected object Constants {
        const val resource = "android"
        const val simpleDeployId = "simple_0"
        const val weakDeployId = "weak_0"
    }

    // Variables
    protected val zetaPushService by lazy {
        ZetaPushService(businessId, simpleDeployId, weakDeployId, resource, storageCredentialHandler, storageTokenHandler, options)
    }

    /**
     * Return the ZetaPush client
     *
     * @return
     */
    val zetaPushClient: ZetapushClient?
        get() = zetaPushService.zetaPushClient

    /**
     * Get the user key of an user
     *
     * @return : User key as a string
     */
    val userKey: String?
        get() = zetaPushService.userKey


    /**
     * Check if the user is connected to the ZetaPush platform
     *
     * @return : true if connected, false if not
     */
    val isConnected: Boolean
        get() = zetaPushService.isConnected

    /**
     * Get the Sandbox ID
     *
     * @return : sandbox id as string
     */
    val sandboxId: String?
        get() = zetaPushService.sandboxId

    /**
     * Get the resource
     *
     * @return : Resource as string
     */
    /**
     * Set the resource
     *
     * @param resource : new resource
     */
    var resource: String?
        get() = zetaPushService.resource
        set(resource) {
            zetaPushService.resource = resource
        }

    /**
     * Launch the disconnection to the ZetaPush platform
     */
    fun disconnect(completion: (() -> Unit)? = null) {
        zetaPushService.disconnection(completion)
    }

    /**
     * Do checking before connection
     */
    fun canDoConnection(): Boolean {
        return !zetaPushService.isConnected
    }

    fun addConnectionListener(listener: ConnectionStatusListener) {
        zetaPushService.listener = listener
    }

    /**
     * Set the storage handler for the token
     *
     * @param storageTokenHandler : Class which implements the interface
     */
    private fun setStorageTokenHandler(storageTokenHandler: StorageTokenInterface) {
        zetaPushService.setTokenStorageHandler(storageTokenHandler)
    }

    /**
     * Set the storage handler for the credentials
     *
     * @param storageCredentialsHandler : Class which implements the interface
     */
    private fun setStorageCredentialsHandler(storageCredentialsHandler: StorageCredentialsInterface) {
        zetaPushService.setCredentialsStorageHandler(storageCredentialsHandler)
    }
}
