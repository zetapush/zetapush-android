package com.zetapush.testsdk;

import android.util.Log;

import com.zetapush.client.ConnectionStatusListener;

import java.util.Map;


public class LogConnectionStatusListener implements ConnectionStatusListener {

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
