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
    private final IBinder mBinder = new ZPServiceBinder();

    ZetapushClient zpClient= null;

    public ZetaPushService() {
    }

    public class ZPServiceBinder extends Binder {
        ZetaPushService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ZetaPushService.this;
        }
    }

    public boolean initService(String businessId, ZetapushHandshakeManager zpHM, ConnectionStatusListener csListener, String resource){
        Log.d("initService", "enter");
        if (zpClient==null){
            zpClient = ZetapushClientFactory.create(businessId,zpHM, resource);
            zpClient.addConnectionStatusListener(csListener);
            try {
                zpClient.start();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public ZetapushClient getZpClient(){
        return zpClient;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
