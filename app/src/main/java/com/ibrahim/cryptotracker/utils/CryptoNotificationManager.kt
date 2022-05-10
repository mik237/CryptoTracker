package com.ibrahim.cryptotracker.utils

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ibrahim.cryptotracker.R
import com.ibrahim.cryptotracker.ui.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoNotificationManager @Inject constructor(){

    fun notifyUser(title:String, descriptions:String, notificationId: Int, context: Context){
        val channelId = "crypto_notification_channel"
        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(context, MainActivity::class.java)


        val pendingIntent: PendingIntent?
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        else{
            @SuppressLint("UnspecifiedImmutableFlag")
            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(descriptions)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setContentIntent(pendingIntent)
            setStyle(NotificationCompat.BigTextStyle().bigText(descriptions))
            priority = NotificationCompat.PRIORITY_MAX
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager.getNotificationChannel(channelId) == null){
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Crypto Tracking Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Crypto Tracking Notification"
                    setShowBadge(false)
                }

                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}