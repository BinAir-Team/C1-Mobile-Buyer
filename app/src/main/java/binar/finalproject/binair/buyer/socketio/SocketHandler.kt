package binar.finalproject.binair.buyer.socketio

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketHandler {
    private const val uri = "https://binair-backend-production.up.railway.app/"
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(uri)
        } catch (e: Exception) {
            Log.e("SocketHandler", e.message.toString())
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}