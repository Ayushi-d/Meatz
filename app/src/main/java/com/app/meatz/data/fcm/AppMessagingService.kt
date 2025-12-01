package com.app.meatz.data.fcm

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Intent
import android.util.Log
import android.window.SplashScreen
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.app.meatz.data.application.ORDER_ID
import com.app.meatz.data.preferences.notificationsAllowed
import com.app.meatz.presentation.home.MainActivity
import com.app.meatz.presentation.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

private const val TAG = "FCM"
private const val MANAGER_REQUEST_CODE = 100
private const val ORDER_REQUEST_CODE = 200

class AppMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Firebase token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Firebase message: ${remoteMessage.data}")
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "${remoteMessage.data}")
            sendNotification(remoteMessage.data)
        }
    }

    private fun sendNotification(data: Map<String, String>) {
        val notification = NotificationUtils(applicationContext)
        val stackBuilder = TaskStackBuilder.create(this)
        Log.i("dataa", data.get("body").toString())
        when (data["click_action"]) {
            "order" -> {
                if (notificationsAllowed.not()) return
                val resultIntent = intentFor<MainActivity>(ORDER_ID to data["id"]).clearTask().newTask()
                stackBuilder.addNextIntent(resultIntent)
                val pendingIntent =
                        stackBuilder.getPendingIntent(ORDER_REQUEST_CODE, FLAG_MUTABLE or PendingIntent.FLAG_ONE_SHOT)
                with(NotificationManagerCompat.from(this)) {
                    pendingIntent?.let {
                        notify(ORDER_REQUEST_CODE, notification.show(data.getValue("body"), it))
                    }
                }
            }
            else -> {
                if (notificationsAllowed.not()) return
                val resultIntent = intentFor<SplashActivity>()
                stackBuilder.addNextIntent(resultIntent)
                val pendingIntent =
                        stackBuilder.getPendingIntent(MANAGER_REQUEST_CODE, FLAG_IMMUTABLE)
                with(NotificationManagerCompat.from(this)) {
                    pendingIntent?.let {
                        notify(MANAGER_REQUEST_CODE, notification.show(data.getValue("body"), it))
                    }
                }
            }
        }
    }
}