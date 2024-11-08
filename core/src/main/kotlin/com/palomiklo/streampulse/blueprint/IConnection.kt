package com.palomiklo.streampulse.blueprint

interface IConnection {
    fun sendEvent(event: String)
    fun closeConnection()
    fun isConnected(): Boolean
}
