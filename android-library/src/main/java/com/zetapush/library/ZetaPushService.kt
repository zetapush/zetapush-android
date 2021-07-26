package com.zetapush.library

import android.util.Log
import com.zetapush.client.ConnectionStatusListener
import com.zetapush.client.highlevel.ZetapushClient
import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory
import com.zetapush.client.highlevel.factories.ZetapushClientFactory
import com.zetapush.common.messages.ZetaApiError
import com.zetapush.library.storages.credentials.Credentials
import com.zetapush.library.storages.credentials.StorageCredentialsInterface
import com.zetapush.library.storages.token.StorageTokenInterface
import java.lang.Exception

class ZetaPushService(
    private val businessId: String,
    private val simpleDeployId: String,
    private val weakDeployId: String,
    private val resourceId: String,
    private var storageCredentialsHandler: StorageCredentialsInterface,
    private var storageTokenHandler: StorageTokenInterface,
    private val options: Map<String, String>
) : ConnectionStatusListener {

    private var disconnectCompletion: (() -> Unit)? = null

    var listener: ConnectionStatusListener? = null

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
     * @return : Credentials with key : 'login' and 'password'
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
     * Set/Get the resource
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
     * Migrate tokens to a secured storage if needed. Remove this method after 01/01/2022
     */
    @Deprecated(message = "Remove this method after 01/01/2022")
    fun migrateTokensAndCredentialsToSecuredStorageIfNeeded() {
        storageCredentialsHandler.migrateToSecuredStorageIfNeeded()
        storageTokenHandler.migrateToSecuredStorageIfNeeded()
    }

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
     * @param login      : Login for authentication
     * @param password   : Password for authentication
     */
    fun connectionAsSimpleAuthentication(login: String, password: String) {
        storageCredentialsHandler.saveCredentials(login, password)

        Log.d("ZP", "connectionAsSimpleAuthentication")
        zetaPushClient = ZetapushClientFactory.create(
            null, null,
            businessId,
            ZetapushAuthentFactory.createSimpleHandshake(login, password, simpleDeployId),
            options,
            resourceId
        )
        zetaPushClient?.addConnectionStatusListener(this)
        zetaPushClient?.start()
    }

    /**
     * Create ZetaPush client and launch connection as Simple Authentication
     */
    fun connectionAsWeakAuthentication() {

        val token = storageTokenHandler.token

        Log.d("ZP", "connectionAsWeakAuthentication")

        zetaPushClient = ZetapushClientFactory.create(
            null, null,
            businessId,
            ZetapushAuthentFactory.createWeakHandshake(token, weakDeployId),
            options,
            resourceId
        )
        zetaPushClient?.addConnectionStatusListener(this)
        zetaPushClient?.start()


        zetaPushClient?.handshake(ZetapushAuthentFactory.createWeakHandshake(token, weakDeployId))
    }

    /**
     * Launch a disconnection to the ZetaPush
     */
    fun disconnection(completion: (() -> Unit)? = null) {

        disconnectCompletion = completion

        try {
            storageCredentialsHandler.clearCredentials()
            storageTokenHandler.clearToken()
        } catch (error: Exception) {
            Log.e("ZP", "error during clearing credentials")
        }

        zetaPushClient?.stop()
    }

    override fun successfulHandshake(map: Map<String, Any>) {

        Log.d("ZP", "successfulHandshake -> map = $map")
        storageTokenHandler.saveToken(map["token"] as String?)
        listener?.successfulHandshake(map)
    }

    override fun failedHandshake(s: String, zetaApiError: ZetaApiError) {
        Log.d("ZP", "failedHandshake -> message = $s, error = $zetaApiError")
        // TODO: Implementation to save map when failed handshake

        listener?.failedHandshake(s, zetaApiError)
    }

    override fun connectionEstablished() {
        Log.d("ZP", "connectionEstablished")
        isConnected = true
        listener?.connectionEstablished()
    }

    override fun connectionBroken() {
        Log.d("ZP", "connectionBroken")
        isConnected = false
        listener?.connectionBroken()
    }

    override fun connectionClosed() {
        Log.d("ZP", "connectionClosed")
        isConnected = false
        disconnectCompletion?.invoke()
        listener?.connectionClosed()
    }

    override fun messageLost(s: String, o: Any) {
        Log.d("ZP", "messageLost -> message = $s, object = $o")
        listener?.messageLost(s, o)
    }
}
