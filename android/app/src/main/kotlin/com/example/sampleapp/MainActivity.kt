package com.example.sampleapp

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.sampleapp.broadcastreceivers.PhoneNumberReceiver
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            val requests = mutableListOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                requests.add(Manifest.permission.POST_NOTIFICATIONS)

            requestPermissions(requests.toTypedArray(), 1)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("RequestPermissionResult", grantResults[0].toString())
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        Flutter.flutterEngine = flutterEngine
        Log.d("configureFlutterEngine ", "platformChannel.toString()")

        val phoneNumberReceiver = PhoneNumberReceiver()
        val intentFilter = IntentFilter("android.intent.action.PHONE_STATE")
        registerReceiver(phoneNumberReceiver, intentFilter)
    }
}

object Flutter {
    lateinit var flutterEngine: FlutterEngine
}