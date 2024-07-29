package com.example.habitwave

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleAlarms(context)
        }
    }

    private fun rescheduleAlarms(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet: Set<String> = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, emptySet()) ?: emptySet()

        for (habit in favoritesSet) {
            // Example habit string format: "Title - 15-10-2023 - 10:30 - 1,2,3"
            val parts = habit.split(" - ")
            if (parts.size == 4) {
                val title = parts[0]
                val dateParts = parts[1].split("-")
                val timeParts = parts[2].split(":")
                val daysOfWeek = parts[3].split(",").map { it.toInt() }

                val year = dateParts[2].toInt()
                val month = dateParts[1].toInt() - 1 // Calendar months are 0-based
                val dayOfMonth = dateParts[0].toInt()
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()

                setRepeatingAlarms(context, year, month, dayOfMonth, hour, minute, daysOfWeek, title)
            }
        }
    }

    private fun setRepeatingAlarms(
        context: Context,
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int,
        daysOfWeek: List<Int>,
        title: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        daysOfWeek.forEach { dayOfWeek ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                set(Calendar.DAY_OF_WEEK, dayOfWeek)
            }

            // If the set time is before the current time, add one week
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("title", title)
                putExtra("time", "${calendar.time}")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                dayOfWeek,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

    }
}