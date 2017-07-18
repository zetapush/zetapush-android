package com.zetapush.testsdk;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection zetapushServiceConnection = new ZetaPushServiceAnonymousAuthenticationServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // init an intent that will create the ZetaPushService
        Intent intent = new Intent(this, ZetaPushService.class);
        // Bind to LocalService
        bindService(intent, zetapushServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        Log.d("onStop", "onStop");
        unbindService(zetapushServiceConnection);
    }

}
