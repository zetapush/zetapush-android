package com.zetapush.library

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
