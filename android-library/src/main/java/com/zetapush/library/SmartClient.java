package com.zetapush.library;

import android.app.Activity;

import java.util.Map;

/**
 * Created by damien on 19/07/17.
 */

public class SmartClient extends Client {

    private String resource_default = "android";
    private String deployId_default_simple = "simple_0";
    private String deployId_default_weak = "weak_0";

    // Constructor
    public SmartClient(Activity activity) {
        super(activity);
    }

    public SmartClient(Activity activity, StorageTokenInterface storageTokenHandler) {
        super(activity, storageTokenHandler);
    }

    public SmartClient(Activity activity, StorageCredentialsInterface storageCredentialHandler) {
        super(activity, storageCredentialHandler);
    }

    public SmartClient(Activity activity, StorageTokenInterface storageTokenHandler, StorageCredentialsInterface storageCredentialHandler) {
        super(activity, storageTokenHandler, storageCredentialHandler);
    }


    /**
     * Basic Weak Authentication
     * @param businessId : Sandbox ID
     */
    public void connect(String businessId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId_default_weak, resource_default);
    }

    /**
     * Weak Authentication with deployment ID
     * @param businessId : Sandbox ID
     * @param deployId : Value of the authentication service
     */
    public void connect(String businessId, String deployId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, resource_default);
    }


    /**
     * Basic Simple Authentication
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     */
    public void connect(String businessId, String login, String password) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsSimpleAuthentication(businessId, login, password, deployId_default_simple, resource_default);
    }

    /**
     * Simple Authentication with deploy ID
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     * @param deployId : Authentication service
     */
    public void connect(String businessId, String login, String password, String deployId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsSimpleAuthentication(businessId, login, password, deployId, resource_default);
    }

    /**
     * Simple Authentication with deploy ID and resouce
     * @param businessId : Sandbox ID
     * @param login : Login
     * @param password : Password
     * @param deployId : Authentication service
     * @param resource : Resource
     */
    public void connect(String businessId, String login, String password, String deployId, String resource) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsSimpleAuthentication(businessId, login, password, deployId, resource);
    }

    /**
     * Get credentials (login and password)
     * @return : Map with key 'login' and 'password'
     */
    public Map<String, String> getCredentials() {
        if (super.zetaPushService == null) return null;
        return super.zetaPushService.getCredentials();
    }
    /**
     * Check if the user has credentials
     * @return : true if he has credentials, false if not
     */
    public boolean hasCredentials() {
        if (super.zetaPushService == null) return false;
        Map<String, String> credentials = super.zetaPushService.getCredentials();

        return (credentials.get("login") != null);
    }


    /**
     * Check of the user is strongly authenticated
     * @return : true if strongly authenticated, false if not
     */
    public boolean isStronglyAuthenticated() {
        if (super.zetaPushService == null) return false;
        return (this.hasCredentials() && super.isConnected());
    }


    /**
     * Check of the user is weakly authenticated
     * @return : true if weakly authenticated, false if not
     */
    public boolean isWeaklyAuthenticated() {
        if (super.zetaPushService == null) return false;
        return (!this.hasCredentials() && super.isConnected());
    }

}
