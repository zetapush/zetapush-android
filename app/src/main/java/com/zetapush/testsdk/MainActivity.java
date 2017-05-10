package com.zetapush.testsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zetapush.client.ConnectionStatusListener;
import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ZetaPushService mZPService = null;
    private boolean mBound = false;

    static final String BUSINESSID = "YOUR_BUSINESS_ID";
    static final String WEAK_DEP_ID = "YOUR_WEAK_DEP_ID";
    static final String RESOURCE= "android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ZetaPushService.class);
        //startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            Log.d("onStop", "onStop");
            unbindService(mConnection);
            mBound = false;
        }
    }
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.d("ServiceConnection", "onServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ZetaPushService.ZPServiceBinder binder = (ZetaPushService.ZPServiceBinder) service;
            mZPService = binder.getService();
            mBound = true;
            new Thread(new Runnable() {
                public void run() {
                    mZPService.initService(BUSINESSID, ZetapushAuthentFactory.createWeakHandshake(BUSINESSID, WEAK_DEP_ID), new ZPConnectionListener(), RESOURCE);
                }
            }).start();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.d("ServiceConnection", "onServiceDisconnected");
        }
    };

    private class ZPConnectionListener implements ConnectionStatusListener {

        @Override
        public void successfulHandshake(Map<String, Object> map) {
            Log.d("successfulHandshake", "ok");
        }

        @Override
        public void failedHandshake(Map<String, Object> map) {

        }

        @Override
        public void connectionEstablished() {
            Log.d("connectionEstablished", "ok");
        }

        @Override
        public void connectionBroken() {
            Log.d("connectionBroken", "ok");
        }

        @Override
        public void connectionClosed() {
            Log.d("connectionClosed", "ok");
        }

        @Override
        public void messageLost(String s, Object o) {
            Log.d("messageLost", s);
        }
    }
}
