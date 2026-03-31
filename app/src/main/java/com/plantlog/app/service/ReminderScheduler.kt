package com.plantlog.app.service

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 提醒任务调度器
 * 配置和调度定期的浇水提醒检查
 */
@Singleton
class ReminderScheduler @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val WORK_NAME = "water_reminder_work"
    }
    
    /**
     * 设置定期提醒
     * 每天早上 9 点检查需要浇水的植物
     */
    fun scheduleDailyReminder() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()
        
        // 配置定期工作
        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            repeatInterval = Duration.ofDays(1)
        )
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    
    /**
     * 取消提醒
     */
    fun cancelReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
    
    /**
     * 计算到明天早上 9 点的延迟
     */
    private fun calculateInitialDelay(): Long {
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = now
            set(java.util.Calendar.HOUR_OF_DAY, 9)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            
            // 如果已经过了今天 9 点，则设置为明天 9 点
            if (timeInMillis <= now) {
                add(java.util.Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        return calendar.timeInMillis - now
    }
}
