package com.zetapush.testsdk;

import android.app.Activity;

/**
 * Created by damien on 19/07/17.
 */

public class WeakClient extends Client {

    private String resource_default = "android";
    private String deployId_default = "weak_0";

    public WeakClient(Activity activity) {
        super(activity);
    }

    public WeakClient(Activity activity, StorageTokenInterface storageTokenHandler) {
        super(activity, storageTokenHandler);
    }

    public WeakClient(Activity activity, StorageCredentialsInterface storageCredentialHandler) {
        super(activity, storageCredentialHandler);
    }

    public WeakClient(Activity activity, StorageTokenInterface storageTokenHandler, StorageCredentialsInterface storageCredentialHandler) {
        super(activity, storageTokenHandler, storageCredentialHandler);
    }



    /**
     * Basic Weak Authentication
     * @param businessId : Sandbox ID
     */
    public void connect(String businessId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId_default, resource_default);
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
     * Weak Authentication with deployment ID and resource
     * @param businessId : Sandbox ID
     * @param deployId : Value of the authentication service
     * @param resource : Resource
     */
    public void connect(String businessId, String deployId, String resource) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, resource);
    }

    /**
     * Get the token for the weak authentication
     * @return : Token as a string
     */
    public String getToken() {
        if (super.zetaPushService == null) return null;
        return super.zetaPushService.getToken();
    }
}
