package binar.finalproject.binair.buyer.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.makeNotification
import binar.finalproject.binair.buyer.databinding.ActivityMainBinding
import binar.finalproject.binair.buyer.socketio.SocketHandler
import dagger.hilt.android.AndroidEntryPoint
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSocketIO()
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navBuyTicket -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navPromo -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_promoFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navHistory -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_ticketHistoryFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navWishlist -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_wishlistFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navProfile -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_profileFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun initSocketIO(){
        val idUser = getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("idUser", null)
        val onNewNotif = Emitter.Listener {
            if(it != null){
                runOnUiThread {
                    val arrObj = it[0] as JSONArray
                    val obj = arrObj.get(arrObj.length() - 1) as JSONObject
                    val msg = obj.getString("message")
                    var title = ""
                    if(msg.contains("Login")) {
                        title = "Login"
                    }else if(msg.contains("Transaksi")){
                        title = "Pembelian Tiket"
                    }else if(msg.contains("Pembayaran")){
                        title = "Status Pembayaran"
                    }
                    makeNotification(title,msg,this)
                }
            }
        }
        SocketHandler.setSocket()
        if(idUser != null){
            val mSocket = SocketHandler.getSocket()
            mSocket.connect()
            mSocket.emit("create",idUser)
            mSocket.on("notify-update", onNewNotif)
        }
    }
}