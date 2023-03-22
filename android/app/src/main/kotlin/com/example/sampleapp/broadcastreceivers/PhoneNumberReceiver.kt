package com.example.sampleapp.broadcastreceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sampleapp.R
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class PhoneNumberReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals("android.intent.action.PHONE_STATE")) {
            return
        }

        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

        Log.d("Intent state", state.toString())
        Log.d("Ring state", TelephonyManager.EXTRA_STATE_RINGING)

        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            val incomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.d("IncomingCallReceiver", "Incoming call from: $incomingNumber")

            if (incomingNumber != null) {
                // To make notification in android
                makeNotification(context, incomingNumber)

                // To send data to Flutter
                sendToFlutterMain(context, incomingNumber)
            }
        }
    }

    private fun makeNotification(context: Context?, incomingNumber: String) {
        val notificationManager = ContextCompat.getSystemService(
            context!!,
            NotificationManager::class.java
        ) as NotificationManager

        val notificationId = 0
        val channelId = "channelId"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(incomingNumber)
            .setContentText("You received call from this number")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun sendToFlutterMain(context: Context?, incomingNumber: String) {
        val platformChannel =
            MethodChannel(FlutterEngine(context!!).dartExecutor.binaryMessenger, "kotlin_channel")
        platformChannel.invokeMethod("kotlin_method", incomingNumber)
    }
}