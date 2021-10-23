package com.example.atyabtabkha

import android.util.Log
import com.huawei.hms.push.HmsMessageService

class MyPushService : HmsMessageService() {
    val TAG = "PushDemoLog"
    override fun onNewToken(token: String) {
        Log.i(TAG, "receive token:$token")
    }

}