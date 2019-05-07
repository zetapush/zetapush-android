package com.zetapush.library

import android.content.Context
import com.zetapush.client.highlevel.ZetapushClient

open class Client {

    // Variables
    protected var zetaPushService: ZetaPushService

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
     * Constructor for a ZetaPush Client
     */
    constructor(context: Context) {
        val storageTokenHandler = KeyValueTokenStorage(context, "key_storage_token")
        val storageCredentialHandler = KeyValueCredentialsStorage(context, "key_storage_login", "key_storage_password")
        this.zetaPushService = ZetaPushService(storageCredentialHandler, storageTokenHandler)
    }

    /**
     * Constructor for a ZetaPush Client : Define storage token handler
     *
     * @param storageTokenHandler
     */
    constructor(context: Context, storageTokenHandler: StorageTokenInterface) {
        val storageCredentialHandler = KeyValueCredentialsStorage(context, "key_storage_login", "key_storage_password")
        this.zetaPushService = ZetaPushService(storageCredentialHandler, storageTokenHandler)
    }

    /**
     * Constructor for a ZetaPush Client : Define storage credentials handler
     *
     * @param storageCredentialHandler
     */
    constructor(context: Context, storageCredentialHandler: StorageCredentialsInterface) {
        val storageTokenHandler = KeyValueTokenStorage(context, "key_storage_token")
        this.zetaPushService = ZetaPushService(storageCredentialHandler, storageTokenHandler)
    }

    /**
     * Constructor for a ZetaPush Client : Define storage token and credentials handlers
     *
     * @param storageTokenHandler
     * @param storageCredentialHandler
     */
    constructor(storageTokenHandler: StorageTokenInterface, storageCredentialHandler: StorageCredentialsInterface) {
        this.zetaPushService = ZetaPushService(storageCredentialHandler, storageTokenHandler)
    }

    /**
     * Launch the disconnection to the ZetaPush platform
     */
    fun disconnect() {
        zetaPushService.disconnection()
    }

    /**
     * Do checking before connection
     */
    fun canDoConnection(): Boolean {
        return !zetaPushService.isConnected
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
