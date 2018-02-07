package com.zetapush.library;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.zetapush.client.ConnectionStatusListener;
import com.zetapush.client.highlevel.ZetapushClient;
import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory;
import com.zetapush.client.highlevel.factories.ZetapushClientFactory;

import java.util.Map;

public class ZetaPushService extends Service {

    private final Binder binder = new ZetaPushBinder();
    private ZetapushClient zpClient = null;

    public static final String          FLAG_ACTION_BROADCAST       = "flag_action_broadcast";
    public static final String          FLAG_STATE_CONNECTION       = "flag_state_connection";
    public static final String          FLAG_SUCCESSFUL_HANDSHAKE   = "flag_successfulhandshake";
    public static final String          FLAG_FAILED_HANDSHAKE       = "flag_failed_handshake";
    public static final String          FLAG_CONNECTION_ESTABLISHED = "flag_connection_established";
    public static final String          FLAG_CONNECTION_BROKEN      = "flag_connection_broken";
    public static final String          FLAG_CONNECTION_CLOSED      = "flag_connection_closed";
    public static final String          FLAG_MESSAGE_LOST           = "flag_message_lost";

    private boolean                     IS_CONNECTED                = false;

    private StorageCredentialsInterface storageCredentialsHandler;
    private StorageTokenInterface       storageTokenHandler;

    public ZetaPushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class ZetaPushBinder extends Binder{
        ZetaPushService getService() {
            return (ZetaPushService.this);
        }
    }


    /**
     * Return the ZetaPush client
     * @return
     */
    public ZetapushClient getZetaPushClient() {
        return this.zpClient;
    }

    /**
     * Set the handler to store credentials
     * @param storageCredentialsHandler : class which implements the interface
     */
    public void setCredentialsStorageHandler(StorageCredentialsInterface storageCredentialsHandler) {
        this.storageCredentialsHandler = storageCredentialsHandler;
    }

    /**
     * Set the handler to store token
     * @param storageTokenHandler : class which implements the interface
     */
    public void setTokenStorageHandler(StorageTokenInterface storageTokenHandler) {
        this.storageTokenHandler = storageTokenHandler;
    }

    /**
     * Get the token from the storage
     * @return
     */
    public String getToken() {
        if (this.storageTokenHandler == null) return null;
        return this.storageTokenHandler.getToken();
    }

    /**
     * Get the credentials from the storage
     * @return : map with key : 'login' and 'password'
     */
    public Map<String, String> getCredentials() {
        if (this.storageCredentialsHandler == null) return null;
        return this.storageCredentialsHandler.getCredentials();
    }


    /**
     * Create ZetaPush client and launch connection as Weak Authentication
     * @param businessId : Sandbox ID
     * @param login : Login for authentication
     * @param password : Password for authentication
     * @param deployId : Deploy ID for Authentication Service
     * @param resource : Resource
     */
    public void connectionAsSimpleAuthentication(final String businessId, final String login, final String password, final String deployId, final String resource) {
        storageCredentialsHandler.saveCredentials(login, password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                zpClient = ZetapushClientFactory.create(businessId, ZetapushAuthentFactory.createSimpleHandshake(login, password, deployId), resource);
                zpClient.addConnectionStatusListener(new zpConnectionListener());
                zpClient.start();
            }
        }).start();
    }

    /**
     * Create ZetaPush client and launch connection as Simple Authentication
     * @param businessId : Sandbox ID
     * @param deployId : Deploy ID for Authentication Service
     * @param resource : Resouce
     */
    public void connectionAsWeakAuthentication(final String businessId, final String deployId, final String resource) {

        final String token = storageTokenHandler.getToken();

        new Thread(new Runnable() {
            @Override
            public void run() {
                zpClient = ZetapushClientFactory.create(businessId, ZetapushAuthentFactory.createWeakHandshake(token, deployId), resource);
                zpClient.addConnectionStatusListener(new zpConnectionListener());
                zpClient.start();
            }
        }).start();
    }

    /**
     * Launch a disconnection to the ZetaPush
     */
    public void disconnection() {

        storageCredentialsHandler.clearCredentials();
        storageTokenHandler.clearToken();

        new Thread(new Runnable() {
            @Override
            public void run() {
                zpClient.stop();
            }
        }).start();
    }

    /**
     * Check if the client is connected to the ZetaPush platform
     * @return : true if connected, false if not
     */
    public boolean isConnected() {
        return IS_CONNECTED;
    }

    /**
     * Get the Sandbox ID
     * @return : sandbox id as string
     */
    public String getSandboxId() {
        return zpClient.getBusinessId();
    }

    /**
     * Get the resource
     * @return : Resource as string
     */
    public String getResource() {
        return zpClient.getResource();
    }

    /**
     * Get the userKey of the connected user
     * @return : Userkey as string
     */
    public String getUserKey() {
        return zpClient.getUserId();
    }

    /**
     * Set the resource
     * @param resource : new resource
     */
    public void setResource(String resource) {
        zpClient.setResource(resource);
    }



    /**
     *  Listener for the ZetaPush Connection
     */
    private class zpConnectionListener implements ConnectionStatusListener {

        @Override
        public void successfulHandshake(Map<String, Object> map) {

            storageTokenHandler.saveToken((String)map.get("token"));

            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_SUCCESSFUL_HANDSHAKE);
            sendBroadcast(i);
        }

        @Override
        public void failedHandshake(Map<String, Object> map) {

            // TODO: Implementation to save map when failed handshake

            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_FAILED_HANDSHAKE);
            sendBroadcast(i);
        }

        @Override
        public void connectionEstablished() {
            IS_CONNECTED = true;

            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_CONNECTION_ESTABLISHED);
            sendBroadcast(i);
        }

        @Override
        public void connectionBroken() {
            IS_CONNECTED = false;

            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_CONNECTION_BROKEN);
            sendBroadcast(i);
        }

        @Override
        public void connectionClosed() {
            IS_CONNECTED = false;

            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_CONNECTION_CLOSED);
            sendBroadcast(i);
        }

        @Override
        public void messageLost(String s, Object o) {
            Intent i = new Intent(FLAG_ACTION_BROADCAST);
            i.putExtra(FLAG_STATE_CONNECTION, FLAG_MESSAGE_LOST);
            sendBroadcast(i);
        }
    }
}
