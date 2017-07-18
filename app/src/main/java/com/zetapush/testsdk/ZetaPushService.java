package com.zetapush.testsdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.zetapush.client.ConnectionStatusListener;
import com.zetapush.client.highlevel.ZetapushClient;
import com.zetapush.client.highlevel.ZetapushHandshakeManager;
import com.zetapush.client.highlevel.factories.ZetapushClientFactory;


public class ZetaPushService extends Service {
    private final IBinder binder = new ZPServiceBinder();

    ZetapushClient zetapushClient = null;

    public ZetaPushService() {
    }

    public class ZPServiceBinder extends Binder {
        public ZetaPushService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ZetaPushService.this;
        }
    }

    /**
     * TODO: this component should be directly provided by the SDK
     * TODO: Do not return a boolean, keep the exception in order to know that there is an error and why
     *
     * Initializes the ZetaPush client:
     * <ul>
     * <li>Creates a handshake manager (for handling connection and
     * authentication)</li>
     * <li>Registers a listener to be notified of the connection status
     * (connected, disconnected, ...)</li>
     * <li>Connects to the ZetaPush server using the provided business/sandbox
     * identifier</li>
     * </ul>
     *
     * @param businessId
     *            the business/sandbox identifer to connect to
     * @param handshakeManager
     *            the handshake manager used to handle connection and
     *            authentication
     * @param csListener
     *            a listener to be notified about the status of the connection
     * @param resource
     *            an optional resource to identify the type of the client for
     *            example
     * @return true if the connection is initiated, false if something went
     *         wrong
     */
    public boolean initService(String businessId, ZetapushHandshakeManager handshakeManager, ConnectionStatusListener csListener, String resource){
        Log.d("initService", "enter");
        if (zetapushClient == null){
            zetapushClient = ZetapushClientFactory.create(businessId, handshakeManager, resource);
            zetapushClient.addConnectionStatusListener(csListener);
            try {
                zetapushClient.start();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public ZetapushClient getZetapushClient(){
        return zetapushClient;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
