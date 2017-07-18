package com.zetapush.testsdk;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory;


public class ZetaPushServiceLoginPasswordAuthenticationServiceConnection implements ServiceConnection {
    static final String BUSINESSID = "YOUR_BUSINESS_ID";
    static final String SIMPLE_DEP_ID = "YOUR_SIMPLE_DEP_ID";
    static final String RESOURCE= "android";

    private ZetaPushService zetapushService = null;

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        Log.d("ServiceConnection", "onServiceConnected");
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        ZetaPushService.ZPServiceBinder binder = (ZetaPushService.ZPServiceBinder) service;
        zetapushService = binder.getService();
        new Thread(new Runnable() {
            public void run() {
                zetapushService.initService(BUSINESSID, ZetapushAuthentFactory.createSimpleHandshake("[user login]", "[user password]", SIMPLE_DEP_ID), new LogConnectionStatusListener(), RESOURCE);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        Log.d("ServiceConnection", "onServiceDisconnected");
    }
}
