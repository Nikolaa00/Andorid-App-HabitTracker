package com.example.habittrackerapp.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.habittrackerapp.data.receiver.ReminderReceiver
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.util.NotificationConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun scheduleReminders(habit: Habit) {
        habit.reminders.forEach { time ->
            habit.frequency.forEach { dayOfWeekInt ->
                scheduleReminder(habit, time, dayOfWeekInt)
            }
        }
    }

    private fun scheduleReminder(habit: Habit, time: LocalTime, dayOfWeekInt: Int) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(NotificationConstants.EXTRA_HABIT_NAME, habit.name)
            putExtra(NotificationConstants.EXTRA_HABIT_ID, habit.id)
        }

        // Unique ID for each reminder per habit per day per time
        val requestCode = (habit.id.toString() + time.toString() + dayOfWeekInt).hashCode()
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Convert our 1-7 (Mon-Sun) to java.util.Calendar days
        val calendarDay = when (dayOfWeekInt) {
            1 -> Calendar.MONDAY
            2 -> Calendar.TUESDAY
            3 -> Calendar.WEDNESDAY
            4 -> Calendar.THURSDAY
            5 -> Calendar.FRIDAY
            6 -> Calendar.SATURDAY
            7 -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, calendarDay)
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If the time has already passed today, schedule for next week
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        if (canScheduleExactAlarms()) {
            Log.d("ReminderScheduler", "Scheduling EXACT reminder for ${habit.name} at ${calendar.time}")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            Log.d("ReminderScheduler", "Scheduling INEXACT (fallback) reminder for ${habit.name} at ${calendar.time}")
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminders(habit: Habit) {
        Log.d("ReminderScheduler", "Cancelling all reminders for ${habit.name}")
        habit.reminders.forEach { time ->
            habit.frequency.forEach { dayOfWeekInt ->
                val intent = Intent(context, ReminderReceiver::class.java)
                val requestCode = (habit.id.toString() + time.toString() + dayOfWeekInt).hashCode()
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                pendingIntent?.let {
                    alarmManager.cancel(it)
                    it.cancel()
                }
            }
        }
    }
}
