# ZetaPush Android library
This library manage all the complex connection process for you. You will find utility classes to simplify the processe of connecting to ZetaPush and calling services and macros.

# ZetaPush Android sample

Example of Android project that uses ZetaPush Android SDK

## Install

### Prerequisite

First of all, [you have to create an account on ZetaPush](https://doc.zetapush.com/quickstart/)

### Add ZetaPush Android SDK dependency

Add ZetaPush Nexus repository (`'http://nexus.zpush.io:8080/repository/zetandroid/'`) to your main Gradle file (`build.gradle` at root of your Android project):

![build.gradle](https://user-images.githubusercontent.com/645363/28027104-4d585dca-6598-11e7-97b8-82a9e1c698bf.jpg)

```gradle
...
allprojects {
    repositories {
        jcenter()
        maven {
            url "http://nexus.zpush.io:8080/repository/zetandroid/"
        }
    }
}
...
```

Then add the ZetaPush Android dependency `'compile com.zetapush:android-library:+'` to the application gradle file (`app/build.gradle`):

![app/build.gradle](https://user-images.githubusercontent.com/645363/28027256-c62b6a6c-6598-11e7-8245-2e0758b5eb23.jpg)

```gradle
...

dependencies {
    ...
    
    compile 'compile com.zetapush:android-library:+'
}
...
```


### Make it work with Android linter

Some dependencies embedded by ZetaPush SDK references classes that are not available in Android.
The classes that are using missing classes are never used by ZetaPush SDK. The classes are mainly
utilities provided by external libraries.

To disable lint checking, add `lintOptions` section in android configuration (in `app/build.gradle` file):

![app/build.gradle and app/lint.xml](https://user-images.githubusercontent.com/645363/28027103-4d576d66-6598-11e7-8162-9566b205342b.jpg)

```gradle
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

# ZetaPush Android Example

## Usage

### Authentication

To launch the connection to the ZetaPush platform, we need to have a **ZetaPush Client**. You need to create either a **WeakClient** or a **SmartClient**. The first one provides an anonymous authentication and the second one provides both an anonymous authentication and a simple authentication (we can choose during to connection).

#### WeakClient
```java
    WeakClient client = new WeakClient(MainActivity.this);
    client.connect(SANDBOX_ID);
    // or client.connect(SANDBOX_ID, DEPLOY_ID);
    // or client.connect(SANDBOX_ID, DEPLOY_ID, resource);
```
The `SANDBOX_ID` is the ID of your sandbox on the ZetaPush platform. Then, the `DEPLOY_ID` is the id of your authentication service, by default this is *weak_0*. The `resource` is a string to identify your application.

#### SmartClient
```java
    SmartClient client = new SmartClient(MainActivity.this);
    client.connect(SANDBOX_ID);
    // or client.connect(SANDBOX_ID, DEPLOY_ID);
    // or client.connect(SANDBOX_ID, LOGIN, PASSWORD);
    // or client.connect(SANDBOX_ID, LOGIN, PASSWORD, DEPLOY_ID);
    // or client.connect(SANDBOX_ID, LOGIN, PASSWORD, DEPLOY_ID, resource);
```

The variables are the same that the `WeakClient`. The `LOGIN` and the `PASSWORD` are the credentials for the user in his simple authentication. If you put credentials you will connect you as simple authentication, if not, as weak authentication.


#### Disconnection

    client.disconnect();


#### ConnectionListener

To get the status connection, you need to create a BroadcastReceiver and listen on the differents events :


```java
    private ZetaPushConnectionReceiver zetaPushReceiver = new ZetaPushConnectionReceiver();


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


    private class ZetaPushConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getStringExtra(ZetaPushService.FLAG_STATE_CONNECTION)) {

                case ZetaPushService.FLAG_CONNECTION_ESTABLISHED:
                    Log.e("STATE CONNECTION", "Connection established");
                    break;

                case ZetaPushService.FLAG_CONNECTION_CLOSED:
                    Log.e("STATE CONNECTION", "Connection closed");
                    break;
            }
        }
    }
```

For example, here we only listen on the `ConnectionEstablished` and `ConnectionClosed` events. Here the list of connection events :

*   SuccessfullHandshake
*   FailedHandshake
*   ConnectionEstablished
*   ConnectionBroken
*   ConnectionClosed
*   MessageLost


### Macroscripts

To call macroscripts, you need to generate classes from your ZetaPush project. For this, you need to use the command line like this :

    zms sdk -l java -i com.zetapush.monprojet -o /tmp/output_folder

// TODO : Provide the CLI and explain how to install it

Then you put the folder beside the package of your Activity in your Android code. When the classes are imported, you need to use a specific API to call macroscripts :

*   Asynchrone API
*   Synchrone API
*   Future API

The name of your interfaces and classes are specifics to your project but you have an example for each case below. Because we use communication network you need to launch the macroscript calls outside the UI Thread.


#### Utils

To properly understand how to call macroscripts, we need to define some variables. First when we use `"macro_0"`, this is the name of the macroscript service in your ZetaPush project. By default this is *macro_0*. The variable `client` is your `WeakClient` or `SmartClient` object.

Then, the macroscript used to make our tests is named `welcome` :
```java
    /**
    * Takes a message as input, and returns it, with a server message
    */
    macroscript welcome(/** message from the client */ string message = "Hello") {
        // ...
    } return {clientMessage : message, serverMessage : WELCOME_MESSAGE}
```

#### Synchrone API

```java
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                MacroSyncApi macroApi = MacroSyncApi.Factory.createService(client.getZetaPushClient(), "macro_0");
                welcomeCompletion resultMacro = macroApi.welcome(new welcomeInput("test"));
                Log.e("RESULT MACRO", resultMacro.getResult().toString());
            }
        }
    }).start();
```

#### Asynchrone API

In this case, you need to create a listener that implements the AsyncApiListener.

```java
    private class MacroApiListener implements MacroAsyncApiListener {

        @Override
        public void welcome(welcomeCompletion notification) {
            Log.e("RESULT WELCOME", notification.getResult().toString());
        }
    }
```

Then we can call our macroscripts :

```java
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                MacroAsyncApi macroApi = MacroAsyncApi.Factory.createService(client.getZetaPushClient(), "macro_0");
                MacroAsyncApiListener.Factory.registerListener(new MacroApiListener(), client.getZetaPushClient(), "macro_0");
                macroApi.welcome(new welcomeInput("test"));
            }
        }
    }).start();
```

#### Future API
```java
    private Future<welcomeCompletion> resultMacro;


    ...


    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                MacroFutureApi macroApi = MacroFutureApi.Factory.createService(client.getZetaPushClient(), "macro_0");
                resultMacro = macroApiFuture.welcome(new welcomeInput("test"));
            }
        }
    }).start();


    ...


    try {
        welcomeCompletion output = resultMacro.get();
        Log.e("RESULT MACRO", output.getResult().toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
```

### Storage

<<<<<<< HEAD
### Authentication
=======
The Android SDK provides you the possibility to choose the method to save the credentials and the token used in simple authentication and weak authentication respectively.

By default the informations are saved using the Key-Value method of the Android system. 
You change this when you create your client. Here the signature of `WeakClient` and `SmartClient` constructors :

    SmartClient(Activity);
    SmartClient(Activity, StorageTokenInterface);
    SmartClient(Activity, StorageCredentialsInterface);
    SmartClient(Activity, StorageTokenInterface, StorageCredentialsInterface);
>>>>>>> high_level

    WeakClient(Activity);
    WeakClient(Activity, StorageTokenInterface);
    WeakClient(Activity, StorageCredentialsInterface);
    WeakClient(Activity, StorageTokenInterface, StorageCredentialsInterface);


For example if you don't want to save any data, you can use the `NoTokenStorage` and `NoCredentialsStorage` classes that implement the needed interfaces :
```java
    SmartClient client = new SmartClient(MainActivity.this, new NoTokenStorage(), new NoCredentialsStorage());
```
Of course you also can choose to implements yourself the methods to save, get and delete data. For example if you want to change the method to handle token :

```java
    SmartClient client = new SmartClient(MainActivity.this, new StorageTokenInterface() {
        @Override
        public void saveToken(String token) {

        }

        @Override
        public String getToken() {
            return null;
        }

        @Override
        public void clearToken() {

        }
    });
```

### Secondary methods


The clients implements many other explicit methods. We list them :

#### Client (superclass of WeakClient and SmartClient)

*   getUserKey()
*   isConnected()
*   canDoConnection()
*   getSandboxId()
*   getResource()
*   setResource()

#### SmartClient

*   getCredentials()
*   hasCredentials()
*   isStronglyAuthenticated()
*   isWeaklyAuthenticated()


#### WeakClient

*   getToken()



### Basic example

Here is a basic example with a button to launch the connection and another to call a macroscript. We use in our case the asynchrone API and a SmartClient.

```java
    public class MainActivity extends Activity {

        private Button                          btnConnection;
        private Button                          btnMacro;
        private Thread                          threadMacro;


        // ZetaPush variables
        private SmartClient                     client;
        private ZetaPushConnectionReceiver      zetaPushReceiver = new ZetaPushConnectionReceiver();
        private final String                    SANDBOX_ID       = "nL_L8ZqL";
        private final String                    LOGIN            = "user";
        private final String                    PASSWORD         = "password";



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Create the ZetaPush Client
            client = new SmartClient(MainActivity.this);

            // UI
            btnConnection = (Button) findViewById(R.id.btnConnection);
            btnMacro = (Button) findViewById(R.id.btnMacro);

            // buttons
            btnConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the connection to the ZetaPush platform
                    client.connect(SANDBOX_ID, LOGIN, PASSWORD);
                }
            });

            btnMacro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // ZetaPush code to call macroscript (Asynchrone)
                    threadMacro = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MacroAsyncApi macroApi = MacroAsyncApi.Factory.createService(client.getZetaPushClient(), "macro_0");
                            MacroAsyncApiListener.Factory.registerListener(new MacroApiListener(), client.getZetaPushClient(), "macro_0");
                            macroApi.welcome(new welcomeInput("test"));
                        }
                    });
                    threadMacro.start();
                }
            });
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

        @Override
        protected void onDestroy() {
            client.disconnect();
            try {
                threadMacro.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onDestroy();
        }

        // BroadcastReceiver for the connection status of ZetaPush
        private class ZetaPushConnectionReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getStringExtra(ZetaPushService.FLAG_STATE_CONNECTION)) {

                    case ZetaPushService.FLAG_CONNECTION_ESTABLISHED:
                        Log.e("STATE CONNECTION", "Connection established");
                        break;

                    case ZetaPushService.FLAG_CONNECTION_CLOSED:
                        Log.e("STATE CONNECTION", "Connection closed");
                        break;
                }
            }
        }

        private class MacroApiListener implements AndroidpreprodAsyncApiListener {

            @Override
            public void welcome(welcomeCompletion notification) {
                Log.e("RESULT MACRO", notification.getResult().toString());
            }
        }
    }
```

### How to share ZetaPush service between Activities

We have created an utility class call ZetaPushBaseActivity than you can use to help you share the ZetaPush service between your activities. If you have your main activity that create the WeakClient or SmarClient object, you can share the ZetaPushService by simply overiding onServiceAttached and onServiceDetached event.

If you have to create an API listener for your activity, don't forget to use a thread to instantiate it.


```java
public class ChildActivity extends ZetaPushBaseActivity implements ZetaPushConnectionEvent {

    private ZetapushClient zetapushClient;
    private String userId;
    private static final String TAG = ChildActivity.class.getSimpleName();

    @Override
    protected void onServiceAttached(ZetaPushService service) {
        // do something necessary by its subclass.
        Log.d(TAG, "onServiceAttached");
        zetapushClient = service.getZetaPushClient();
        //userId = zetapushClient.getUserId();

        new Thread() {
            public void run() {
                try {
                    // Create here your futureAPI or API Listeners ?
                    //futureApi = VisioFutureApi.Factory.createService(zetapushClient, "macro_0");
                    //VisioAsyncApiListener.Factory.registerListener(ZetapushConnectActivity.this, zetapushClient, "macro_0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void onServiceDetached() {
        Log.d(TAG, "onServiceDetached");
        new Thread() {
            public void run() {
                try {
                    // Unregister your API Listeners
                    //VisioAsyncApiListener.Factory.unregisterListener(ZetapushConnectActivity.this, zetapushClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void successfulHandshake(Map<String, Object> map) {
        Log.d(TAG, "successfulHandshake");
    }

    @Override
    public void failedHandshake(Map<String, Object> map) {
        Log.d(TAG, "failedHandshake");
    }

    @Override
    public void connectionEstablished() {
        Log.d(TAG, "connectionEstablished");
    }

    @Override
    public void connectionBroken() {
        Log.d(TAG, "connectionBroken");
    }

    @Override
    public void connectionClosed() {
        Log.d(TAG, "connectionClosed");
    }

    @Override
    public void messageLost(String s, Object o) {

    }
}
```

