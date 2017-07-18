# ZetaPush Android sample

Example of Android project that uses ZetaPush Android SDK

## Install

### Prerequisite

First of all, [you have to create an account on ZetaPush](https://doc.zetapush.com/quickstart/)

### Add ZetaPush Android SDK dependency

Add ZetaPush Nexus repository (`'http://nexus.zpush.io:8080/repository/public/'`) to your main Gradle file (`build.gradle` at root of your Android project):

![build.gradle](https://user-images.githubusercontent.com/645363/28027104-4d585dca-6598-11e7-97b8-82a9e1c698bf.jpg)

```groovy
...
allprojects {
    repositories {
        jcenter()
        maven {
            url "http://nexus.zpush.io:8080/repository/public/"
        }
    }
}
...
```

Then add the ZetaPush Android dependency `'com.zetapush:android-client:[2.6.7, )'` to the application gradle file (`app/build.gradle`):

![app/build.gradle](https://user-images.githubusercontent.com/645363/28027256-c62b6a6c-6598-11e7-8245-2e0758b5eb23.jpg)

```groovy
...

dependencies {
    ...
    
    compile 'com.zetapush:android-client:[2.6.7, )'
}
...
```

The dependency uses a version range (`[2.6.7, )`). It means that the minimum version of ZetaPush SDK is 2.6.7 and there is no maximum version.

For a real project, it is better to fix the version (`2.6.7` instead of `[2.6.7, )`).


### Make it work with Android linter

Some dependencies embedded by ZetaPush SDK references classes that are not available in Android.
The classes that are using missing classes are never used by ZetaPush SDK. The classes are mainly
utilities provided by external libraries.

To disable lint checking, add `lintOptions` section in android configuration (in `app/build.gradle` file):

![app/build.gradle and app/lint.xml](https://user-images.githubusercontent.com/645363/28027103-4d576d66-6598-11e7-8162-9566b205342b.jpg)

```groovy
android {
    ...

    lintOptions {
        lintConfig file("lint.xml")
    }
}
```

Creates a file named `lint.xml` in `app` directory with the following content:

```xml
<lint>
    <issue id="InvalidPackage">
        <ignore path="**/android-client*.jar"/>
    </issue>
</lint>
```

## Usage

### Initialization

To provide the ZetaPush client as an Android service, you have to create a class with the following content:

```java
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

	ZetapushClient zpClient = null;

	public ZetaPushService() {
	}

	public class ZPServiceBinder extends Binder {
		ZetaPushService getService() {
			// Return this instance of LocalService so clients can call public methods
			return ZetaPushService.this;
		}
	}

	/**
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
	public boolean initService(String businessId, ZetapushHandshakeManager handshakeManager, ConnectionStatusListener csListener, String resource) {
		Log.d("initService", "enter");
		if (zpClient == null) {
			zpClient = ZetapushClientFactory.create(businessId, handshakeManager, resource);
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

	public ZetapushClient getZpClient() {
		return zpClient;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
```

The service is now declared and ready to be used. The `initService` method is used to initiate a connection to the ZetaPush backend.
Connection to the ZetaPush backend requires a business/sandbox identifier that you get by [creating a sandbox](https://doc.zetapush.com/quickstart/).
During the connection to the server (handshake), an authentication is required. There exists several [authentication mechanisms](#authentication) (authentication will be described later).

Now, to use the Android service in your activity, you have to ask Android to give you an instance of that service:

```java

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
```

The `Intent` is used to indicate that Android has to create the ZetaPush service.
The `ServiceConnection` is used to know when the Android service is bound to the activity. The initialization of ZetaPush happens in that `ServiceConnection`:

```java

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory;


public class FakeServiceConnection implements ServiceConnection {

    private ZetaPushService zetapushService = null;

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        Log.d("ServiceConnection", "onServiceConnected");
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        ZetaPushService.ZPServiceBinder binder = (ZetaPushService.ZPServiceBinder) service;
        zetapushService = binder.getService();
        new Thread(new Runnable() {
            public void run() {
                zetapushService.initService(...);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        Log.d("ServiceConnection", "onServiceDisconnected");
    }
}
```

The `initService` method is called in order to initiate the connection to the ZetaPush backend. The connection is done using your business/sandbox identifier (replace `BUSINESSID` value by your business/sandbox identifier).
See [Authentication for more information about how to connect and authenticate to ZetaPush backend](#authentication).

(#authentication) 
### Authentication

#### Anonymous authentication

Initialization of ZetaPush service uses a `ZetapushHandshakeManager` to handle the authentication. The previous sample uses a weak (anonymous) authentication to ZetaPush backend:

```java

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zetapush.client.highlevel.factories.ZetapushAuthentFactory;


public class ZetaPushServiceAnonymousAuthenticationServiceConnection implements ServiceConnection {
    static final String BUSINESSID = "YOUR_BUSINESS_ID";
    static final String WEAK_DEP_ID = "YOUR_WEAK_DEP_ID";
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
                zetapushService.initService(BUSINESSID, 
                                            ZetapushAuthentFactory.createWeakHandshake(BUSINESSID, WEAK_DEP_ID), 
                                            new LogConnectionStatusListener(), 
                                            RESOURCE);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        Log.d("ServiceConnection", "onServiceDisconnected");
    }
}
```

You have to replace `BUSINESSID` value by your business/sandbox identifier and `WEAK_DEP_ID` by the identifier of your weak authentication service (deployed in your sandbox).


#### Login/password authentication

If you want to authenticate your users, you may need to use a login/password pair. The following sample shows how to define a `ServiceConnection` that uses:

```java

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
                zetapushService.initService(BUSINESSID, 
                                            ZetapushAuthentFactory.createSimpleHandshake("<login>", "<password>", SIMPLE_DEP_ID), 
                                            new LogConnectionStatusListener(), 
                                            RESOURCE);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        Log.d("ServiceConnection", "onServiceDisconnected");
    }
}
```

You have to replace `BUSINESSID` value by your business/sandbox identifier and `SIMPLE_DEP_ID` by the identifier of your simple authentication service (deployed in your sandbox).
