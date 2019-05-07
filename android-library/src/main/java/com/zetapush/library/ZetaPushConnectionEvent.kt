package com.zetapush.library

/**
 * Created by mikaelmorvan on 06/02/2018.
 */

/**
 * Callback interface for ZetaPush connection.
 */
interface ZetaPushConnectionEvent {

    fun successfulHandshake(map: Map<String, Any>?)

    fun failedHandshake(map: Map<String, Any>?)

    fun connectionEstablished()

    fun connectionBroken()

    fun connectionClosed()

    fun messageLost(s: String?, o: Any?)
}
