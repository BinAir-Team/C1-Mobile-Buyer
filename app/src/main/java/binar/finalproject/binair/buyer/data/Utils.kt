package binar.finalproject.binair.buyer.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import binar.finalproject.binair.buyer.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun formatRupiah(value: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(value)
}

fun formatDateFromISO(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val newDate = formatter.parse(date)
    val formatter2 = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return formatter2.format(newDate)
}

fun makeNotification(title : String, message : String, context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationId = System.currentTimeMillis().toInt()
    val channelId = "channel-01"
    val channelName = "My Channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(
            channelId, channelName, importance
        )
        notificationManager.createNotificationChannel(mChannel)
    }

    val mBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)

    notificationManager.notify(notificationId, mBuilder.build())
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

