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

class MainActivity : FlutterActivity() {
    private val phoneNumberReceiver = PhoneNumberReceiver()

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

        val intentFilter = IntentFilter("android.intent.action.PHONE_STATE")
        registerReceiver(phoneNumberReceiver, intentFilter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("RequestPermissionResult", grantResults[0].toString())
    }
}
