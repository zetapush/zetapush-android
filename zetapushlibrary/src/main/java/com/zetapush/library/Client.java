package com.zetapush.library;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zetapush.client.highlevel.ZetapushClient;

/**
 * Created by damien on 19/07/17.
 */

public class Client {

    // Variables
    protected ZetaPushService zetaPushService = null;
    private StorageTokenInterface storageTokenHandler = null;
    private StorageCredentialsInterface storageCredentialHandler = null;

    /**
     * Constructor for a ZetaPush Client
     * @param activity : Activity
     */
    public Client(Activity activity) {
        Intent intent = new Intent(activity, ZetaPushService.class);
        activity.bindService(intent, onService, Context.BIND_AUTO_CREATE);
        this.storageTokenHandler = new KeyValueTokenStorage(activity, "key_storage_token");
        this.storageCredentialHandler = new KeyValueCredentialsStorage(activity, "key_storage_login", "key_storage_password");
    }


    /**
     * Constructor for a ZetaPush Client : Define storage token handler
     * @param activity
     * @param storageTokenHandler
     */
    public Client(Activity activity, StorageTokenInterface storageTokenHandler) {
        Intent intent = new Intent(activity, ZetaPushService.class);
        activity.bindService(intent, onService, Context.BIND_AUTO_CREATE);
        this.storageTokenHandler = storageTokenHandler;
        this.storageCredentialHandler = new KeyValueCredentialsStorage(activity, "key_storage_login", "key_storage_password");
    }

    /**
     * Constructor for a ZetaPush Client : Define storage credentials handler
     * @param activity
     * @param storageCredentialHandler
     */
    public Client(Activity activity, StorageCredentialsInterface storageCredentialHandler) {
        Intent intent = new Intent(activity, ZetaPushService.class);
        activity.bindService(intent, onService, Context.BIND_AUTO_CREATE);
        this.storageTokenHandler = new KeyValueTokenStorage(activity, "key_storage_token");
        this.storageCredentialHandler = storageCredentialHandler;
    }

    /**
     * Constructor for a ZetaPush Client : Define storage token and credentials handlers
     * @param activity
     * @param storageTokenHandler
     * @param storageCredentialHandler
     */
    public Client(Activity activity, StorageTokenInterface storageTokenHandler, StorageCredentialsInterface storageCredentialHandler) {
        Intent intent = new Intent(activity, ZetaPushService.class);
        activity.bindService(intent, onService, Context.BIND_AUTO_CREATE);
        this.storageTokenHandler = storageTokenHandler;
        this.storageCredentialHandler = storageCredentialHandler;
    }

    /**
     * Return the ZetaPush client
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
     * @return : true if connected, false if not
     */
    public boolean isConnected() {
        if (zetaPushService == null) return false;
        return zetaPushService.isConnected();
    }

    /**
     * Get the Sandbox ID
     * @return : sandbox id as string
     */
    public String getSandboxId() {
        if (zetaPushService == null) return null;
        return zetaPushService.getSandboxId();
    }

    /**
     * Get the resource
     * @return : Resource as string
     */
    public String getResource() {
        if (zetaPushService == null) return null;
        return zetaPushService.getResource();
    }

    /**
     * Set the resource
     * @param resource : new resource
     */
    public void setResource(String resource) {
        if (zetaPushService == null) return;
        zetaPushService.setResource(resource);
    }

    /**
     * Set the storage handler for the token
     * @param storageTokenHandler : Class which implements the interface
     */
    private void setStorageTokenHandler(StorageTokenInterface storageTokenHandler) {
        if (zetaPushService == null) return;
        zetaPushService.setTokenStorageHandler(storageTokenHandler);
    }

    /**
     * Set the storage handler for the credentials
     * @param storageCredentialsHandler : Class which implements the interface
     */
    private void setStorageCredentialsHandler(StorageCredentialsInterface storageCredentialsHandler) {
        if (zetaPushService == null) return;
        zetaPushService.setCredentialsStorageHandler(storageCredentialsHandler);
    }

    // Service connection for the ZetaPushService
    private ServiceConnection onService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnection", "onServiceConnected");
            zetaPushService = ((ZetaPushService.ZetaPushBinder) service).getService();
            setStorageTokenHandler(storageTokenHandler);
            setStorageCredentialsHandler(storageCredentialHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceConnection", "onServiceDisconnected");
            zetaPushService = null;
        }
    };
}
