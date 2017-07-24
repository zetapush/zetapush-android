package com.zetapush.testsdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zetapush.client.highlevel.factories.ZetapushClientFactory;

public class MainActivity extends Activity {

    // VARIABLES
    private SmartClient                     client;
    private ZetaPushConnectionReceiver      zetaPushReceiver = new ZetaPushConnectionReceiver();

    private final String                    SANDBOX_ID       = "nL_L8ZqL";
    private final String                    LOGIN            = "user";
    private final String                    PASSWORD         = "password";

    private Button                          btnConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZetapushClientFactory.setRootUrl("http://vm-zbo:8080/zbo/pub/business");

        btnConnection = (Button) findViewById(R.id.btnConnection);

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.connect(SANDBOX_ID, LOGIN, PASSWORD);
            }
        });

        // Create client
        client = new SmartClient(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(zetaPushReceiver, new IntentFilter(ZetaPushService.FLAG_ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(zetaPushReceiver);
    }

    // BroadcastReceiver for the connection status
    private class ZetaPushConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getStringExtra(ZetaPushService.FLAG_STATE_CONNECTION)) {

                case ZetaPushService.FLAG_CONNECTION_ESTABLISHED:
                    Toast.makeText(MainActivity.this, "Connection established", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
