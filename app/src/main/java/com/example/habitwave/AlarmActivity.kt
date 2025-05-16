package com.example.habitwave



import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.habitwave.databinding.ActivityAlarmBinding
import java.util.Calendar


class AlarmActivity : AppCompatActivity() {

    companion object {

        private  const val REQUEST_CODE = 1
        private const val REQUEST_CODE_EXACT_ALARM = 1
        private const val PREFS_NAME = "habit_prefs"
        private const val PREFS_KEY_FAVORITES = "favorites"
    }

    private lateinit var binding: ActivityAlarmBinding
    private var habitTitle: String? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "AlarmActivity"
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check for exact alarm permission if running on Android 12 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkExactAlarmPermission()
            checkNotificationPermission()
//            createNotificationChannel()
        }

//for stop alarm
        binding.stopBtn.setOnClickListener {
            AlarmReceiver.stopAlarm()
            Toast.makeText(this, "Alarm Stopped", Toast.LENGTH_SHORT).show()

        }


        // Load saved habits when the app starts
      binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
          selectAllDays(isChecked)
      }

        binding.notifyMeBtn.setOnClickListener {

            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val year = binding.datePicker.year
            val month = binding.datePicker.month
            val dayOfMonth = binding.datePicker.dayOfMonth

            habitTitle = binding.habitTitle.text.toString()

            val daysOfWeek = mutableListOf<Int>()
            if (binding.checkMonday.isChecked) daysOfWeek.add(Calendar.MONDAY)
            if (binding.checkTuesday.isChecked) daysOfWeek.add(Calendar.TUESDAY)
            if (binding.checkWednesday.isChecked) daysOfWeek.add(Calendar.WEDNESDAY)
            if (binding.checkThursday.isChecked) daysOfWeek.add(Calendar.THURSDAY)
            if (binding.checkFriday.isChecked) daysOfWeek.add(Calendar.FRIDAY)
            if (binding.checkSaturday.isChecked) daysOfWeek.add(Calendar.SATURDAY)
            if (binding.checkSunday.isChecked) daysOfWeek.add(Calendar.SUNDAY)

            // Set repeating alarms and show the alert with the user-set time
            setRepeatingAlarms(this, year, month, dayOfMonth, hour, minute, daysOfWeek,
                habitTitle!!
            )


        }

        binding.saveForLaterBtn.setOnClickListener {

           val habitTitle = binding.habitTitle.text.toString()

            val habitDate = "${binding.datePicker.dayOfMonth}-${binding.datePicker.month + 1}-${binding.datePicker.year}"
            val habitTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"
            val daysOfWeek = mutableListOf<String>()
            if (binding.checkMonday.isChecked) daysOfWeek.add("2")
            if (binding.checkTuesday.isChecked) daysOfWeek.add("3")
            if (binding.checkWednesday.isChecked) daysOfWeek.add("4")
            if (binding.checkThursday.isChecked) daysOfWeek.add("5")
            if (binding.checkFriday.isChecked) daysOfWeek.add("6")
            if (binding.checkSaturday.isChecked) daysOfWeek.add("7")
            if (binding.checkSunday.isChecked) daysOfWeek.add("1")

            val daysString = daysOfWeek.joinToString(",")
            addToFavorites( "$habitTitle - $habitDate - $habitTime --> $daysString" )

            startActivity(Intent(this, SaveHabitActivity::class.java))
        }

    }
    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE)
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

    private  fun selectAllDays(isChecked:Boolean){

        binding.checkMonday.isChecked =isChecked
        binding.checkTuesday.isChecked =isChecked
        binding.checkWednesday.isChecked =isChecked
        binding.checkThursday.isChecked =isChecked
        binding.checkFriday.isChecked =isChecked
        binding.checkSaturday.isChecked =isChecked
        binding.checkSunday.isChecked =isChecked




    }

private fun showAlert(title: String, alarmTimes: List<Calendar>) {
    val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
    val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

    val alarmDetails = alarmTimes.joinToString(separator = "\n") { calendar ->
        "${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}"
    }

    AlertDialog.Builder(this)
        .setTitle("Alarm Scheduled")
        .setMessage(
            "Title: $title\nAt:$alarmDetails"
        )
        .setPositiveButton("OK") { _, _ -> }
        .show()
}
//
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
    val alarmTimes = mutableListOf<Calendar>()

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

//        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
//            putExtra(titleExtra, title)
//            putExtra(messageExtra, "It's time for your habit: $title")
//        }

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
            pendingIntent
        )

        alarmTimes.add(calendar)
    }

    // Show a single alert with the title and all scheduled dates
    showAlert(title, alarmTimes)
}

//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelID, name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }








    private fun addToFavorites(habit: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        favoritesSet.add(habit)
        with(sharedPreferences.edit()) {
            putStringSet(PREFS_KEY_FAVORITES, favoritesSet)
            apply()

        }


    }



}


