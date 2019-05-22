package com.zetapush.testsdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.zetapush.library.SmartClient;
import com.zetapush.library.ZetaPushService;

public class MainActivity extends Activity {

    private SmartClient                     client;
//    private ZetaPushConnectionReceiver      zetaPushReceiver = new ZetaPushConnectionReceiver();

    private Button btnLaunch;
    private Button btnConnect;
    private Button btnDisconnect;

    private EditText etSandboxId;
    private EditText etLogin;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btnLaunch = (Button) findViewById(R.id.buttonLaunch);
//        btnLaunch.setEnabled(false);
//        btnConnect = (Button) findViewById(R.id.buttonConnect);
//        btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
//        btnDisconnect.setEnabled(false);
//
//        etSandboxId = (EditText) findViewById(R.id.editTextSandboxId);
//        etLogin = (EditText) findViewById(R.id.editTextLogin);
//        etPassword = (EditText) findViewById(R.id.editTextPassword);
//
//
//        btnLaunch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchChildActivity();
//            }
//        });
//        btnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                connect();
//            }
//        });
//        btnDisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                disconnect();
//            }
//        });
//
//        // Create client
//        client = new SmartClient(this);
//
//        // Get saved account from previous launch
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        etSandboxId.setText(sharedPref.getString(getString(R.string.saved_sandboxId), ""));
//        etLogin.setText(sharedPref.getString(getString(R.string.saved_login), ""));
//        etPassword.setText(sharedPref.getString(getString(R.string.saved_password), ""));
    }

    private void connect() {

//        if (etSandboxId.getText().toString().matches("") || etLogin.getText().toString().matches("") || etPassword.getText().toString().matches("")) {
//            return;
//        }
//
//        // Save account for futur launch
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.saved_sandboxId), etSandboxId.getText().toString());
//        editor.putString(getString(R.string.saved_login), etLogin.getText().toString());
//        editor.putString(getString(R.string.saved_password), etPassword.getText().toString());
//        editor.commit();

//        client.connect(etSandboxId.getText().toString(), etLogin.getText().toString(), etPassword.getText().toString());
    }

    private void disconnect(){
//        client.disconnect();
    }

    /** Called when the user taps the Send button */
    public void launchChildActivity() {
        Intent intent = new Intent(this, ChildActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(zetaPushReceiver, new IntentFilter(ZetaPushService.FLAG_ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(zetaPushReceiver);
    }

    @Override
    protected void onDestroy(){
//        client.release();// IMPORTANT !!!
        super.onDestroy();
    }
    // BroadcastReceiver for the connection status
//    private class ZetaPushConnectionReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            switch (intent.getStringExtra(ZetaPushService.FLAG_STATE_CONNECTION)) {
//
//                case ZetaPushService.FLAG_CONNECTION_ESTABLISHED:
//                    Toast.makeText(MainActivity.this, "Connection established", Toast.LENGTH_LONG).show();
//                    btnLaunch.setEnabled(true);
//                    btnConnect.setEnabled(false);
//                    btnDisconnect.setEnabled(true);
//                    break;
//                case ZetaPushService.FLAG_CONNECTION_CLOSED:
//                    Toast.makeText(MainActivity.this, "Connection closed", Toast.LENGTH_LONG).show();
//                    btnLaunch.setEnabled(false);
//                    btnConnect.setEnabled(true);
//                    btnDisconnect.setEnabled(false);
//                    break;
//            }
//        }
//    }
}
