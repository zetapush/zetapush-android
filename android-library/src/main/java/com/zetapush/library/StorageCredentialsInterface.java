package com.zetapush.library;

import java.util.Map;

/**
 * Created by damien on 20/07/17.
 */

public interface StorageCredentialsInterface {

    /**
     * Save the credentials used for the simple authentication
     * @param login
     * @param password
     */
    void saveCredentials(String login, String password);

    /**
     * Get the credentials as a map with key 'login' and 'password'
     * @return
     */
    Map<String, String> getCredentials();

    /**
     * Clear the saved credentials
     */
    void clearCredentials();
}
