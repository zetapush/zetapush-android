package com.zetapush.library;

import android.content.Context;
import androidx.annotation.NonNull;
import com.zetapush.client.highlevel.ZetapushClient;

/**
 * Created by damien on 19/07/17.
 */

public class Client {

    // Variables
    protected ZetaPushService zetaPushService = null;

    /**
     * Constructor for a ZetaPush Client
     */
    public Client(@NonNull Context context) {
        this.zetaPushService = new ZetaPushService();
        final KeyValueTokenStorage storageTokenHandler = new KeyValueTokenStorage(context, "key_storage_token");
        final KeyValueCredentialsStorage storageCredentialHandler = new KeyValueCredentialsStorage(context, "key_storage_login", "key_storage_password");
        setStorageTokenHandler(storageTokenHandler);
        setStorageCredentialsHandler(storageCredentialHandler);
    }

    /**
     * Constructor for a ZetaPush Client : Define storage token handler
     *
     * @param storageTokenHandler
     */
    public Client(@NonNull Context context, StorageTokenInterface storageTokenHandler) {
        this.zetaPushService = new ZetaPushService();
        final KeyValueCredentialsStorage storageCredentialHandler = new KeyValueCredentialsStorage(context, "key_storage_login", "key_storage_password");
        setStorageTokenHandler(storageTokenHandler);
        setStorageCredentialsHandler(storageCredentialHandler);
    }

    /**
     * Constructor for a ZetaPush Client : Define storage credentials handler
     *
     * @param storageCredentialHandler
     */
    public Client(@NonNull Context context, StorageCredentialsInterface storageCredentialHandler) {
        this.zetaPushService = new ZetaPushService();
        final KeyValueTokenStorage storageTokenHandler = new KeyValueTokenStorage(context, "key_storage_token");
        setStorageTokenHandler(storageTokenHandler);
        setStorageCredentialsHandler(storageCredentialHandler);
    }

    /**
     * Constructor for a ZetaPush Client : Define storage token and credentials handlers
     *
     * @param storageTokenHandler
     * @param storageCredentialHandler
     */
    public Client(StorageTokenInterface storageTokenHandler, StorageCredentialsInterface storageCredentialHandler) {
        this.zetaPushService = new ZetaPushService();
        setStorageTokenHandler(storageTokenHandler);
        setStorageCredentialsHandler(storageCredentialHandler);
    }

    /**
     * Return the ZetaPush client
     *
     * @return
     */
    public ZetapushClient getZetaPushClient() {
        return zetaPushService.getZetaPushClient();
    }


    /**
     * Launch the disconnection to the ZetaPush platform
     */
    public void disconnect() {
        if (zetaPushService == null) return;
        zetaPushService.disconnection();
    }

    /**
     * Get the user key of an user
     *
     * @return : User key as a string
     */
    public String getUserKey() {
        if (zetaPushService == null) return null;
        return zetaPushService.getUserKey();
    }

    /**
     * Do checking before connection
     */
    public boolean canDoConnection() {
        return (zetaPushService != null && !zetaPushService.isConnected());
    }


    /**
     * Check if the user is connected to the ZetaPush platform
     *
     * @return : true if connected, false if not
     */
    public boolean isConnected() {
        if (zetaPushService == null) return false;
        return zetaPushService.isConnected();
    }

    /**
     * Get the Sandbox ID
     *
     * @return : sandbox id as string
     */
    public String getSandboxId() {
        if (zetaPushService == null) return null;
        return zetaPushService.getSandboxId();
    }

    /**
     * Get the resource
     *
     * @return : Resource as string
     */
    public String getResource() {
        if (zetaPushService == null) return null;
        return zetaPushService.getResource();
    }

    /**
     * Set the resource
     *
     * @param resource : new resource
     */
    public void setResource(String resource) {
        if (zetaPushService == null) return;
        zetaPushService.setResource(resource);
    }

    /**
     * Set the storage handler for the token
     *
     * @param storageTokenHandler : Class which implements the interface
     */
    private void setStorageTokenHandler(StorageTokenInterface storageTokenHandler) {
        if (zetaPushService == null) return;
        zetaPushService.setTokenStorageHandler(storageTokenHandler);
    }

    /**
     * Set the storage handler for the credentials
     *
     * @param storageCredentialsHandler : Class which implements the interface
     */
    private void setStorageCredentialsHandler(StorageCredentialsInterface storageCredentialsHandler) {
        if (zetaPushService == null) return;
        zetaPushService.setCredentialsStorageHandler(storageCredentialsHandler);
    }
}
