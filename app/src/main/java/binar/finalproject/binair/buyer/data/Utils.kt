package binar.finalproject.binair.buyer.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import binar.finalproject.binair.buyer.R
import java.text.NumberFormat
import java.util.*

fun formatRupiah(value: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(value)
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

