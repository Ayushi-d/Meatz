package com.app.meatz.data.fcm

import android.app.Notification
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SPEECH
import android.media.AudioAttributes.USAGE_NOTIFICATION
import android.media.RingtoneManager
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.app.meatz.R

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class NotificationUtils(context: Context) : ContextWrapper(context) {

  private val channelId = packageName

  init {
    createChannel()
  }

  fun show(message: String, resultPendingIntent: PendingIntent): Notification {
    val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_push_notification)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(message)
        .setShowWhen(true)
        .setAutoCancel(true)
        .setContentIntent(resultPendingIntent)
        .setLights(0xFFC9AA26.toInt(), 500, 500)
        .setPriority(PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))

    if (SDK_INT < O) {
      val defaultSoundUri = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION)
      builder.setSound(defaultSoundUri)

      builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
    }
    return builder.build()
  }

  private fun createChannel() {
    if (SDK_INT >= O) {
      val channel = NotificationChannel(channelId, packageName, IMPORTANCE_HIGH).apply {
        enableLights(true)
        setShowBadge(true)
        lightColor = 0xFFC9AA26.toInt()
        lockscreenVisibility = VISIBILITY_PUBLIC
        val defaultSoundUri = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION)
        val attributes = AudioAttributes.Builder()
            .setUsage(USAGE_NOTIFICATION)
            .setContentType(CONTENT_TYPE_SPEECH)
            .build()
        setSound(defaultSoundUri, attributes)

        enableVibration(true)
        vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
      }
      val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
      manager.createNotificationChannel(channel)
    }
  }
}