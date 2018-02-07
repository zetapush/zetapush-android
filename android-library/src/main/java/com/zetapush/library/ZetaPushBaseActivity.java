package com.zetapush.library;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zetapush.client.ConnectionStatusListener;
import com.zetapush.client.highlevel.ZetapushClient;

import java.util.Map;

/**
 * Created by mikaelmorvan on 10/01/2018.
 *
 * Utility activity providing a mean to share a service between Activities
 *
 */

public class ZetaPushBaseActivity extends AppCompatActivity {
    private static final String TAG = ZetaPushBaseActivity.class.getSimpleName();
    protected ZetaPushService mZetaPushService;
    private ZetaPushConnectionEvent connectionEvent;

    protected void initConnectionEvent(ZetaPushConnectionEvent connectionEvent){
        this.connectionEvent = connectionEvent;
    }

    @Override
    protected void onPause(){
        detachService();
        super.onPause();
    }

    @Override
    protected void onResume(){
        attachService();
        super.onResume();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mZetaPushService = ((ZetaPushService.ZetaPushBinder) binder).getService();
            initConnectionListener(mZetaPushService.getZetaPushClient());
            onServiceAttached(mZetaPushService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    private void attachService() {
        Intent service = new Intent(this, ZetaPushService.class);
        bindService(service, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void detachService() {
        unbindService(mServiceConnection);
        mZetaPushService = null;
        onServiceDetached();
    }

    private void initConnectionListener(ZetapushClient zpClient){
        if (this.connectionEvent != null){
            ZPConnectionListener ZPCl = new ZPConnectionListener(this.connectionEvent);
            zpClient.addConnectionStatusListener(ZPCl);
        }
    }

    /** Callback when service attached. */
    protected void onServiceAttached(ZetaPushService service) {
        // do something necessary by its subclass.
    }

    /** Callback when service attached. */
    protected void onServiceDetached() {
        // do something necessary by its subclass.
    }

    /**
     *  Listener for the ZetaPush Connection
     */
    private class ZPConnectionListener implements ConnectionStatusListener {
        ZetaPushConnectionEvent connectionEvent;

        public ZPConnectionListener(ZetaPushConnectionEvent connectionEvent){
            this.connectionEvent = connectionEvent;
        }

        private final String TAG = "zpConnectionListener";

        @Override
        public void successfulHandshake(Map<String, Object> map) {
            Log.e(TAG, "successfulHandshake");
            if (this.connectionEvent != null) {
                this.connectionEvent.successfulHandshake(map);
            }
        }

        @Override
        public void failedHandshake(Map<String, Object> map) {
            Log.e(TAG, "failedHandshake");
            if (this.connectionEvent != null) {
                this.connectionEvent.failedHandshake(map);
            }
        }

        @Override
        public void connectionEstablished() {
            Log.e(TAG, "connectionEstablished");
            if (this.connectionEvent != null) {
                this.connectionEvent.connectionEstablished();
            }
        }

        @Override
        public void connectionBroken() {
            Log.e(TAG, "connectionBroken");
            if (this.connectionEvent != null) {
                this.connectionEvent.connectionBroken();
            }
        }

        @Override
        public void connectionClosed() {
            Log.e(TAG, "connectionClosed");
            if (this.connectionEvent != null) {
                this.connectionEvent.connectionClosed();
            }
        }

        @Override
        public void messageLost(String s, Object o) {
            Log.e(TAG, "messageLost " + s);
            if (this.connectionEvent != null) {
                this.connectionEvent.messageLost(s, o);
            }
        }
    }
}
