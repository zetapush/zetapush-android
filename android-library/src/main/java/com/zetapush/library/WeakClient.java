package com.zetapush.library;

import android.content.Context;

/**
 * Created by damien on 19/07/17.
 */

public class WeakClient extends Client {

    private String resource_default = "android";
    private String deployId_default = "weak_0";

    public WeakClient(Context context) {
        super(context);
    }

    public WeakClient(Context context, StorageTokenInterface storageTokenHandler) {
        super(context, storageTokenHandler);
    }

    public WeakClient(Context context, StorageCredentialsInterface storageCredentialHandler) {
        super(context, storageCredentialHandler);
    }

    public WeakClient(StorageTokenInterface storageTokenHandler, StorageCredentialsInterface storageCredentialHandler) {
        super(storageTokenHandler, storageCredentialHandler);
    }


    /**
     * Basic Weak Authentication
     *
     * @param businessId : Sandbox ID
     */
    public void connect(String businessId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId_default, resource_default);
    }

    /**
     * Weak Authentication with deployment ID
     *
     * @param businessId : Sandbox ID
     * @param deployId   : Value of the authentication service
     */
    public void connect(String businessId, String deployId) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, resource_default);
    }

    /**
     * Weak Authentication with deployment ID and resource
     *
     * @param businessId : Sandbox ID
     * @param deployId   : Value of the authentication service
     * @param resource   : Resource
     */
    public void connect(String businessId, String deployId, String resource) {
        if (!super.canDoConnection()) return;
        super.zetaPushService.connectionAsWeakAuthentication(businessId, deployId, resource);
    }

    /**
     * Get the token for the weak authentication
     *
     * @return : Token as a string
     */
    public String getToken() {
        if (super.zetaPushService == null) return null;
        return super.zetaPushService.getToken();
    }
}
