package com.plantlog.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plantlog.app.R
import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.mapper.toDomain
import com.plantlog.app.presentation.MainActivity

/**
 * 浇水提醒后台任务
 * 定期检查需要浇水的植物并发送通知
 */
class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    // 临时简化：直接创建 Repository 实例
    private val plantRepository = run {
        val db = com.plantlog.app.data.local.PlantLogDatabase.getDatabase(context)
        PlantRepository(db.plantDao(), db.careRecordDao())
    }
    
    companion object {
        const val CHANNEL_ID = "water_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }
    
    override suspend fun doWork(): Result {
        return try {
            // 获取需要浇水的植物
            val plantsNeedWater = plantRepository.getPlantsNeedWater()
            
            if (plantsNeedWater.isNotEmpty()) {
                sendNotification(plantsNeedWater.size)
                Result.success()
            } else {
                Result.success()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    /**
     * 发送浇水提醒通知
     */
    private fun sendNotification(plantCount: Int) {
        createNotificationChannel()
        
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🌱 浇水提醒")
            .setContentText("你有 $plantCount 盆植物需要浇水了")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    /**
     * 创建通知渠道（Android 8.0+）
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "浇水提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "植物浇水提醒通知"
            }
            
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
