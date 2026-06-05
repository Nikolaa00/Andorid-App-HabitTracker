package com.example.habittrackerapp.data.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.habittrackerapp.R
import com.example.habittrackerapp.data.repository.HabitRepository
import com.example.habittrackerapp.util.NotificationConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: HabitRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderReceiver", "Alarm received! Action: ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("ReminderReceiver", "Boot completed. Rescheduling all...")
            rescheduleAllReminders()
            return
        }

        val habitName = intent.getStringExtra(NotificationConstants.EXTRA_HABIT_NAME) ?: "Habit"
        val habitId = intent.getIntExtra(NotificationConstants.EXTRA_HABIT_ID, -1)
        Log.d("ReminderReceiver", "Displaying notification for: $habitName (ID: $habitId)")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.HABIT_REMINDER_CHANNEL_ID,
                context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, NotificationConstants.HABIT_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this exists or use a generic one
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_message, habitName))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(habitId, notification)
    }

    private fun rescheduleAllReminders() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.allHabits.first().forEach { habit ->
                val settings = repository.getSettings(habit.userId).first()
                if (settings?.notificationsEnabled == true) {
                    repository.updateHabit(habit)
                }
            }
        }
    }
}
