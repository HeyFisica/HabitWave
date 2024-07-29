
package com.example.habitwave

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import com.example.habitwave.databinding.ActivityAddBinding
import java.util.Calendar

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var habitTitle: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title ="Add New Alarm"
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habitTitle = intent.getStringExtra("HABIT_TITLE")
        val habitTitleTextView: TextView = findViewById(R.id.habitTitleTextView)
        habitTitleTextView.text = habitTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkExactAlarmPermission()
        }
binding.stopBtn.setOnClickListener {
    AlarmReceiver.stopAlarm()
    Toast.makeText(this,"Alarm Stopped",Toast.LENGTH_SHORT).show()

}
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            setAllDayCheckboxes(isChecked)
        }

        binding.notifyMeBtn.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            val daysOfWeek = mutableListOf<Int>()
            if (binding.checkMonday.isChecked) daysOfWeek.add(Calendar.MONDAY)
            if (binding.checkTuesday.isChecked) daysOfWeek.add(Calendar.TUESDAY)
            if (binding.checkWednesday.isChecked) daysOfWeek.add(Calendar.WEDNESDAY)
            if (binding.checkThursday.isChecked) daysOfWeek.add(Calendar.THURSDAY)
            if (binding.checkFriday.isChecked) daysOfWeek.add(Calendar.FRIDAY)
            if (binding.checkSaturday.isChecked) daysOfWeek.add(Calendar.SATURDAY)
            if (binding.checkSunday.isChecked) daysOfWeek.add(Calendar.SUNDAY)

            setRepeatingAlarms(this, hour, minute, daysOfWeek, habitTitle)
        }

        binding.saveForLaterBtn.setOnClickListener {
            val habitDate = "${binding.datePicker.dayOfMonth}-${binding.datePicker.month + 1}-${binding.datePicker.year}"
            val habitTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"

            val daysOfWeek = mutableListOf<String>()
            if(binding.checkMonday.isChecked) daysOfWeek.add("2")
            if(binding.checkMonday.isChecked) daysOfWeek.add("3")
            if(binding.checkMonday.isChecked) daysOfWeek.add("4")
            if(binding.checkMonday.isChecked) daysOfWeek.add("5")
            if(binding.checkMonday.isChecked) daysOfWeek.add("6")
            if(binding.checkMonday.isChecked) daysOfWeek.add("7")
            if(binding.checkMonday.isChecked) daysOfWeek.add("1")

            val daysString = daysOfWeek.joinToString  (",")
            addToFavorites( "$habitTitle - $habitDate - $habitTime --> $daysString" )
            startActivity(Intent(this,SaveHabitActivity::class.java))




        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkExactAlarmPermission() {
        if (!getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }
            startActivityForResult(intent, REQUEST_CODE_EXACT_ALARM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EXACT_ALARM) {
            // Handle the result of the permission request if needed
        }
    }

    private fun showAlert(title: String?, calendars: List<Calendar>) {
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
        val message = calendars.joinToString(separator = "\n") { calendar ->
            "At: ${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}"
        }

        AlertDialog.Builder(this)
            .setTitle("Alarm Scheduled")
            .setMessage("Title: $title\n$message")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun setRepeatingAlarms(
        context: Context,
        hour: Int,
        minute: Int,
        daysOfWeek: List<Int>,
        title: String?
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendars = mutableListOf<Calendar>()

        daysOfWeek.forEach { dayOfWeek ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Find the next occurrence of the specified day of the week
                while (get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }

                // If the calculated time is in the past, move to the next week
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("HABIT_TITLE", title)
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
//                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )

            calendars.add(calendar)
        }

        // Show one alert after all alarms are set
        showAlert(title, calendars)
    }

    private fun setAllDayCheckboxes(isChecked: Boolean) {
        binding.checkMonday.isChecked = isChecked
        binding.checkTuesday.isChecked = isChecked
        binding.checkWednesday.isChecked = isChecked
        binding.checkThursday.isChecked = isChecked
        binding.checkFriday.isChecked = isChecked
        binding.checkSaturday.isChecked = isChecked
        binding.checkSunday.isChecked = isChecked
    }

    private fun addToFavorites(habit: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        favoritesSet.add(habit)
        with(sharedPreferences.edit()) {
            putStringSet(PREFS_KEY_FAVORITES, favoritesSet)
            apply()
        }
    }

    companion object {
        private const val REQUEST_CODE_EXACT_ALARM = 1
        private const val PREFS_NAME = "habit_prefs"
        private const val PREFS_KEY_FAVORITES = "favorites"
    }
}
