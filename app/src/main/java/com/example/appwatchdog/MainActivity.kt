package com.example.appwatchdog

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.appwatchdog.ui.theme.AppWatchdogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        enableEdgeToEdge()
        setContent {
            AppWatchdogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(innerPadding) { enabled, packageName ->
                        if (!hasUsagePermission(context)) {
                            requestUsagePermission(context)
                        } else {

                            if (enabled) {
                                val intent = Intent(this, WatchDogService::class.java)
                                intent.putExtra("TARGET_APP", packageName)
                                startService(intent)
                            } else {
                                stopService(Intent(this, WatchDogService::class.java))
                            }
                        }
                    }

                }
            }
        }
    }
}

fun requestUsagePermission(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}

fun hasUsagePermission(context: Context): Boolean {
    val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

    val mode = appOpsManager.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )

    return mode == AppOpsManager.MODE_ALLOWED
}