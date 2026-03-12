package com.example.appwatchdog

import android.app.usage.UsageStatsManager
import android.content.Context

object UsageStatsHelper {

    fun getForegroundApp(context: Context): String? {

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE)
                as UsageStatsManager

        val time = System.currentTimeMillis()

        val stats = usm.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 10000,
            time
        )

        if (stats.isNullOrEmpty()) return null

        val recent = stats.maxByOrNull { it.lastTimeUsed }

        return recent?.packageName
    }
}