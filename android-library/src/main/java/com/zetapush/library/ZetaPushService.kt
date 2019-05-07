package com.zetapush.library

import android.util.Log
import com.zetapush.client.ConnectionStatusListener
import com.zetapush.client.highlevel.ZetapushClient
import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory
import com.zetapush.client.highlevel.factories.ZetapushClientFactory
import com.zetapush.common.messages.ZetaApiError

class ZetaPushService(
    private var storageCredentialsHandler: StorageCredentialsInterface,
    private var storageTokenHandler: StorageTokenInterface
) {

    //    private final Binder binder = new ZetaPushBinder();
    /**
     * Return the ZetaPush client
     *
     * @return
     */
    var zetaPushClient: ZetapushClient? = null
        private set

    /**
     * Check if the client is connected to the ZetaPush platform
     *
     * @return : true if connected, false if not
     */
    var isConnected = false
        private set

    /**
     * Get the token from the storage
     *
     * @return
     */
    val token: String?
        get() = this.storageTokenHandler.token

    /**
     * Get the credentials from the storage
     *
     * @return : map with key : 'login' and 'password'
     */
    val credentials: Credentials?
        get() = this.storageCredentialsHandler.credentials

    /**
     * Get the Sandbox ID
     *
     * @return : sandbox id as string
     */
    val sandboxId: String?
        get() = zetaPushClient?.businessId

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
        get() = zetaPushClient?.resource
        set(resource) {
            zetaPushClient?.resource = resource
        }

    /**
     * Get the userKey of the connected user
     *
     * @return : Userkey as string
     */
    val userKey: String?
        get() = zetaPushClient?.userId

    /**
     * Set the handler to store credentials
     *
     * @param storageCredentialsHandler : class which implements the interface
     */
    fun setCredentialsStorageHandler(storageCredentialsHandler: StorageCredentialsInterface) {
        this.storageCredentialsHandler = storageCredentialsHandler
    }

    /**
     * Set the handler to store token
     *
     * @param storageTokenHandler : class which implements the interface
     */
    fun setTokenStorageHandler(storageTokenHandler: StorageTokenInterface) {
        this.storageTokenHandler = storageTokenHandler
    }


    /**
     * Create ZetaPush client and launch connection as Weak Authentication
     *
     * @param businessId : Sandbox ID
     * @param login      : Login for authentication
     * @param password   : Password for authentication
     * @param deployId   : Deploy ID for Authentication Service
     * @param resource   : Resource
     */
    fun connectionAsSimpleAuthentication(
        businessId: String,
        login: String,
        password: String,
        deployId: String,
        resource: String
    ) {
        storageCredentialsHandler.saveCredentials(login, password)
        Thread(Runnable {
            Log.d("ZP", "connectionAsSimpleAuthentication")
            zetaPushClient = ZetapushClientFactory.create(
                businessId,
                ZetapushAuthentFactory.createSimpleHandshake(login, password, deployId),
                resource
            )
            zetaPushClient?.addConnectionStatusListener(ZPConnectionListener())
            zetaPushClient?.start()
        }).start()
    }

    /**
     * Create ZetaPush client and launch connection as Simple Authentication
     *
     * @param businessId : Sandbox ID
     * @param deployId   : Deploy ID for Authentication Service
     * @param resource   : Resouce
     */
    fun connectionAsWeakAuthentication(businessId: String, deployId: String, resource: String) {

        val token = storageTokenHandler.token

        Thread(Runnable {
            Log.d("ZP", "connectionAsWeakAuthentication")

            zetaPushClient = ZetapushClientFactory.create(
                businessId,
                ZetapushAuthentFactory.createWeakHandshake(token, deployId),
                resource
            )
            zetaPushClient?.addConnectionStatusListener(ZPConnectionListener())
            zetaPushClient?.start()
        }).start()
    }

    /**
     * Launch a disconnection to the ZetaPush
     */
    fun disconnection() {

        storageCredentialsHandler.clearCredentials()
        storageTokenHandler.clearToken()

        Thread(Runnable { zetaPushClient?.stop() }).start()
    }


    /**
     * Listener for the ZetaPush Connection
     */
    private inner class ZPConnectionListener : ConnectionStatusListener {

        override fun successfulHandshake(map: Map<String, Any>) {

            Log.d("ZP", "successfulHandshake -> map = $map")
            storageTokenHandler.saveToken(map["token"] as String?)
        }

        override fun failedHandshake(s: String, zetaApiError: ZetaApiError) {
            Log.d("ZP", "failedHandshake -> message = $s, error = $zetaApiError")
            // TODO: Implementation to save map when failed handshake
        }

        override fun connectionEstablished() {
            Log.d("ZP", "connectionEstablished")
            isConnected = true
        }

        override fun connectionBroken() {
            Log.d("ZP", "connectionBroken")
            isConnected = false
        }

        override fun connectionClosed() {
            Log.d("ZP", "connectionClosed")
            isConnected = false
        }

        override fun messageLost(s: String, o: Any) {
            Log.d("ZP", "messageLost -> message = $s, object = $o")
        }
    }
}
