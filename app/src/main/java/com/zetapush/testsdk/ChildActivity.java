package com.zetapush.testsdk;

import android.util.Log;

import com.zetapush.client.highlevel.ZetapushClient;
import com.zetapush.library.ZetaPushService;

import java.util.Map;

/**
 * Created by mikaelmorvan on 06/02/2018.
 */

public class ChildActivity  {

    private ZetapushClient zetapushClient;
    private String userId;
    private static final String TAG = ChildActivity.class.getSimpleName();

//    @Override
//    protected void onServiceAttached(ZetaPushService service) {
//        // do something necessary by its subclass.
//        Log.d(TAG, "onServiceAttached");
//        zetapushClient = service.getZetaPushClient();
//        //userId = zetapushClient.getUserId();
//
//        new Thread() {
//            public void run() {
//                try {
//                    // Create here your futureAPI or API Listeners ?
//                    //futureApi = VisioFutureApi.Factory.createService(zetapushClient, "macro_0");
//                    //VisioAsyncApiListener.Factory.registerListener(ZetapushConnectActivity.this, zetapushClient, "macro_0");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
//
//    @Override
//    protected void onServiceDetached() {
//        Log.d(TAG, "onServiceDetached");
//        new Thread() {
//            public void run() {
//                try {
//                    // Unregister your API Listeners
//                    //VisioAsyncApiListener.Factory.unregisterListener(ZetapushConnectActivity.this, zetapushClient);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    @Override
//    public void successfulHandshake(Map<String, Object> map) {
//        Log.d(TAG, "successfulHandshake");
//    }
//
//    @Override
//    public void failedHandshake(Map<String, Object> map) {
//        Log.d(TAG, "failedHandshake");
//    }
//
//    @Override
//    public void connectionEstablished() {
//        Log.d(TAG, "connectionEstablished");
//    }
//
//    @Override
//    public void connectionBroken() {
//        Log.d(TAG, "connectionBroken");
//    }
//
//    @Override
//    public void connectionClosed() {
//        Log.d(TAG, "connectionClosed");
//    }
//
//    @Override
//    public void messageLost(String s, Object o) {
//
//    }
}
