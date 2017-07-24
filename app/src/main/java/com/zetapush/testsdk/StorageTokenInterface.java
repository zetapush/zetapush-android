package com.zetapush.testsdk;

/**
 * Created by damien on 20/07/17.
 */

public interface StorageTokenInterface {

    /**
     * Save the token used for the Weak Authentication
     * @param token : Token as a string
     */
    void saveToken(String token);

    /**
     * Get the token as a string
     * @return : token
     */
    String getToken();

    /**
     * Clear the saved token
     */
    void clearToken();


}
