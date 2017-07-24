package com.zetapush.testsdk;

/**
 * Created by damien on 20/07/17.
 */

public class NoTokenStorage implements StorageTokenInterface {

    @Override
    public void saveToken(String token) {
        // Do nothing
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public void clearToken() {
        // Do nothing
    }
}
