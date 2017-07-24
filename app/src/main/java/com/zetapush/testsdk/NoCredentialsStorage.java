package com.zetapush.testsdk;

import java.util.Map;

/**
 * Created by damien on 24/07/17.
 */

public class NoCredentialsStorage implements StorageCredentialsInterface {
    @Override
    public void saveCredentials(String login, String password) {
        // Do nothing
    }

    @Override
    public Map<String, String> getCredentials() {
        return null;
    }

    @Override
    public void clearCredentials() {
        // Do nothing
    }
}
