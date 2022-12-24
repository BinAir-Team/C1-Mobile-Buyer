package binar.finalproject.binair.buyer.socketio

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketHandler {
    //    val uri = URLEncoder.encode("https://binair-backend-production.up.railway.app/", "UTF-8")
    val uri = "https://binair-backend-production.up.railway.app/"
    lateinit var mSocket: Socket

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