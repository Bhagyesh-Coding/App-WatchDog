package com.example.appwatchdog

import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class WatchDogService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var targetApp: String? = null
    private var overlayShown = false

    private val checker = object : Runnable {

        override fun run() {

            val currentApp =
                UsageStatsHelper.getForegroundApp(this@WatchDogService)

                if (currentApp == targetApp && !overlayShown) {

                    overlayShown = true

                    val intent = Intent(this@WatchDogService, OverlayActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                }
            if (currentApp != targetApp) {
                overlayShown = false
            }

            handler.postDelayed(this, 5000)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        targetApp = intent?.getStringExtra("TARGET_APP")
        handler.post(checker)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }
}