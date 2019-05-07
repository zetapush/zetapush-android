package com.zetapush.library;

import java.util.Map;

/**
 * Created by mikaelmorvan on 06/02/2018.
 */

/**
 * Callback interface for ZetaPush connection.
 */
public interface ZetaPushConnectionEvent {

    void successfulHandshake(Map<String, Object> map);

    void failedHandshake(Map<String, Object> map);

    void connectionEstablished();

    void connectionBroken();

    void connectionClosed();

    void messageLost(String s, Object o);
}
