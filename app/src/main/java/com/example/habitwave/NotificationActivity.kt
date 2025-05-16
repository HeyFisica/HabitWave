//package  com.example.habitwave
//import NotificationReceiver
//import android.Manifest
//import android.app.AlarmManager
//import android.app.AlertDialog
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.Settings
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import channelID
//import com.example.habitwave.databinding.ActivityNotificationBinding
//import notificationID
//import titleExtra
//import java.util.Calendar
//import java.util.Date
//
//const val REQUEST_CODE_EXACT_ALARM = 101
////const val com.example.habitwave.PREFS_NAME = "HabitPrefs"
////const val com.example.habitwave.PREFS_KEY_FAVORITES = "habit_favorites"
//
//class NotificationActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityNotificationBinding
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNotificationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        createNotificationChannel()
//
//        // Check for notification permission if running on Android 13 or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            checkNotificationPermission()
//        }
//
//        // Check for exact alarm permission if running on Android 12 or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            checkExactAlarmPermission()
//        }
//
//        binding.notifyMeBtn.setOnClickListener {
//            scheduleNotification()
//        }
//
//        binding.saveForLaterBtn.setOnClickListener {
//            val habitTitle = binding.habitTitle.text.toString()
//            val habitDate = "${binding.datePicker.dayOfMonth}-${binding.datePicker.month + 1}-${binding.datePicker.year}"
//            val habitTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"
//            addToFavorites("$habitTitle - $habitDate - $habitTime")
//            startActivity(Intent(this, SaveHabitActivity::class.java))
//        }
//    }
//
//    private fun checkNotificationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE)
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun checkExactAlarmPermission() {
//        if (!getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
//            val intent = Intent().apply {
//                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
//            }
//            startActivityForResult(intent, REQUEST_CODE_EXACT_ALARM)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_EXACT_ALARM) {
//            // Handle the result of the permission request if needed
//        }
//    }
//
//    private fun scheduleNotification() {
//        val intent = Intent(applicationContext, NotificationReceiver::class.java)
//        val title = binding.habitTitle.text.toString()
//
//        intent.putExtra(titleExtra, title)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time, title)
//    }
//
//    private fun showAlert(time: Long, title: String) {
//        val date = Date(time)
//        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
//        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
//
//        AlertDialog.Builder(this)
//            .setTitle("Notification Scheduled")
//            .setMessage("Title: $title\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}")
//            .setPositiveButton("OK") { _, _ -> }
//            .show()
//    }
//
//    private fun getTime(): Long {
//        val minute = binding.timePicker.minute
//        val hour = binding.timePicker.hour
//        val day = binding.datePicker.dayOfMonth
//        val month = binding.datePicker.month
//        val year = binding.datePicker.year
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, day, hour, minute)
//        return calendar.timeInMillis
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        val name = "Notify Channel"
//        val desc = "A Description of the Channel"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(channelID, name, importance)
//        channel.description = desc
//        val notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    private fun addToFavorites(habit: String) {
//        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val favoritesSet = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
//        favoritesSet.add(habit)
//        with(sharedPreferences.edit()) {
//            putStringSet(PREFS_KEY_FAVORITES, favoritesSet)
//            apply()
//        }
//    }
//}
////######For Alarm
////
////package com.example.habitwave
////
////import android.app.AlarmManager
////import android.app.AlertDialog
////import android.app.PendingIntent
////import android.content.Context
////import android.content.Intent
////import android.os.Build
////import android.os.Bundle
////import android.provider.Settings
////import androidx.annotation.RequiresApi
////import androidx.appcompat.app.AppCompatActivity
////import com.example.habitwave.databinding.ActivityNotificationBinding
////import java.util.Calendar
////import java.util.Date
////
////const val REQUEST_CODE = 100
////const val REQUEST_CODE_EXACT_ALARM = 101
////const val com.example.habitwave.PREFS_NAME = "HabitPrefs"
////const val com.example.habitwave.PREFS_KEY_FAVORITES = "habit_favorites"
////
////class NotificationActivity : AppCompatActivity() {
////    private lateinit var binding: ActivityNotificationBinding
////
////    @RequiresApi(Build.VERSION_CODES.O)
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = ActivityNotificationBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////
////        // Check for exact alarm permission if running on Android 12 or higher
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////            checkExactAlarmPermission()
////        }
////
////        binding.notifyMeBtn.setOnClickListener {
////            scheduleAlarm()
////        }
////
////        binding.saveForLaterBtn.setOnClickListener {
////            val habitTitle = binding.habitTitle.text.toString()
////            val habitDate = "${binding.datePicker.dayOfMonth}-${binding.datePicker.month + 1}-${binding.datePicker.year}"
////            val habitTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"
////            addToFavorites("$habitTitle - $habitDate - $habitTime")
////            startActivity(Intent(this, SaveHabitActivity::class.java))
////        }
////    }
////
////    @RequiresApi(Build.VERSION_CODES.S)
////    private fun checkExactAlarmPermission() {
////        if (!getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
////            val intent = Intent().apply {
////                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
////            }
////            startActivityForResult(intent, REQUEST_CODE_EXACT_ALARM)
////        }
////    }
////
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////        if (requestCode == REQUEST_CODE_EXACT_ALARM) {
////            // Handle the result of the permission request if needed
////        }
////    }
////
////    private fun scheduleAlarm() {
////        val intent = Intent(applicationContext, AlarmReceiver::class.java)
////        val title = binding.habitTitle.text.toString()
////
////        intent.putExtra(titleExtra, title)
////
////        val pendingIntent = PendingIntent.getBroadcast(
////            applicationContext, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
////        )
////        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val time = getTime()
////        alarmManager.setExactAndAllowWhileIdle(
////            AlarmManager.RTC_WAKEUP,
////            time,
////            pendingIntent
////        )
////        showAlert(time, title)
////    }
////
////    private fun showAlert(time: Long, title: String) {
////        val date = Date(time)
////        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
////        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
////
////        AlertDialog.Builder(this)
////            .setTitle("Alarm Scheduled")
////            .setMessage("Title: $title\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}")
////            .setPositiveButton("OK") { _, _ -> }
////            .show()
////    }
////
////    private fun getTime(): Long {
////        val minute = binding.timePicker.minute
////        val hour = binding.timePicker.hour
////        val day = binding.datePicker.dayOfMonth
////        val month = binding.datePicker.month
////        val year = binding.datePicker.year
////
////        val calendar = Calendar.getInstance()
////        calendar.set(year, month, day, hour, minute)
////        return calendar.timeInMillis
////    }
////
////    private fun addToFavorites(habit: String) {
////        val sharedPreferences = getSharedPreferences(com.example.habitwave.PREFS_NAME, Context.MODE_PRIVATE)
////        val favoritesSet = sharedPreferences.getStringSet(com.example.habitwave.PREFS_KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
////        favoritesSet.add(habit)
////        with(sharedPreferences.edit()) {
////            putStringSet(com.example.habitwave.PREFS_KEY_FAVORITES, favoritesSet)
////            apply()
////        }
////    }
////}
//
//
////Another method
////package com.example.habitwave
////
////import android.app.AlarmManager
////import android.app.AlertDialog
////import android.app.PendingIntent
////import android.content.Context
////import android.content.Intent
////import android.os.Build
////import android.os.Bundle
////import android.provider.Settings
////import android.widget.Toast
////import androidx.annotation.RequiresApi
////import androidx.appcompat.app.AppCompatActivity
////import com.example.habitwave.databinding.ActivityNotificationBinding
////import java.util.Calendar
////import java.util.Date
////
////const val REQUEST_CODE = 100
////const val REQUEST_CODE_EXACT_ALARM = 101
////const val com.example.habitwave.PREFS_NAME = "HabitPrefs"
////const val com.example.habitwave.PREFS_KEY_FAVORITES = "habit_favorites"
////
////class NotificationActivity : AppCompatActivity() {
////    private lateinit var binding: ActivityNotificationBinding
////
////    @RequiresApi(Build.VERSION_CODES.O)
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = ActivityNotificationBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////
////        // Check for exact alarm permission if running on Android 12 or higher
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////            checkExactAlarmPermission()
////        }
////
////        binding.notifyMeBtn.setOnClickListener {
////            val hour = binding.timePicker.hour
////            val minute = binding.timePicker.minute
////            val year = binding.datePicker.year
////            val month = binding.datePicker.month
////            val dayOfMonth = binding.datePicker.dayOfMonth
////
////
////
////
////            val daysOfWeek = mutableListOf<Int>()
////            if (binding.checkMonday.isChecked) daysOfWeek.add(Calendar.MONDAY)
////            if (binding.checkTuesday.isChecked) daysOfWeek.add(Calendar.TUESDAY)
////            if (binding.checkWednesday.isChecked) daysOfWeek.add(Calendar.WEDNESDAY)
////            if (binding.checkThursday.isChecked) daysOfWeek.add(Calendar.THURSDAY)
////            if (binding.checkFriday.isChecked) daysOfWeek.add(Calendar.FRIDAY)
////            if (binding.checkSaturday.isChecked) daysOfWeek.add(Calendar.SATURDAY)
////            if (binding.checkSunday.isChecked) daysOfWeek.add(Calendar.SUNDAY)
////
////            else{
////                Toast.makeText(this,"iej",Toast.LENGTH_SHORT).show()
////            }
////            setRepeatingAlarms(this, year, month, dayOfMonth, hour, minute, daysOfWeek)
////
////
////        }
////
////        binding.saveForLaterBtn.setOnClickListener {
////            val habitTitle = binding.habitTitle.text.toString()
////            val habitDate =
////                "${binding.datePicker.dayOfMonth}-${binding.datePicker.month + 1}-${binding.datePicker.year}"
////            val habitTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"
////            addToFavorites("$habitTitle - $habitDate - $habitTime")
////            startActivity(Intent(this, SaveHabitActivity::class.java))
////        }
////    }
////
////    @RequiresApi(Build.VERSION_CODES.S)
////    private fun checkExactAlarmPermission() {
////        if (!getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
////            val intent = Intent().apply {
////                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
////            }
////            startActivityForResult(intent, REQUEST_CODE_EXACT_ALARM)
////        }
////    }
////
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////        if (requestCode == REQUEST_CODE_EXACT_ALARM) {
////            // Handle the result of the permission request if needed
////        }
////    }
////
////    private fun showAlert(title: String, calendar: Calendar) {
////        //val date = Date(time)
////        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
////        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
////
////        AlertDialog.Builder(this)
////            .setTitle("Alarm Scheduled")
////            .setMessage("Title: $title\nAt: ${dateFormat.format(calendar.time)} ${timeFormat.format(calendar.time)}")
////            .setPositiveButton("OK") { _, _ -> }
////            .show()
////    }
////
////
////
////
////    private fun setRepeatingAlarms(context: Context, year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int,daysOfWeek:List<Int> ) {
////        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val title = binding.habitTitle.text.toString()
////
////
////        daysOfWeek.forEach { dayOfWeek ->
////            val calendar = Calendar.getInstance().apply {
////                set(Calendar.YEAR, year)
////                set(Calendar.MONTH, month)
////                set(Calendar.DAY_OF_MONTH, dayOfMonth)
////                set(Calendar.HOUR_OF_DAY, hour)
////                set(Calendar.MINUTE, minute)
////                set(Calendar.SECOND, 0)
////                set(Calendar.MILLISECOND, 0)
////                set(Calendar.DAY_OF_WEEK, dayOfWeek)
////            }
////
////            // If the set time is before the current time, add one week
////            if (calendar.timeInMillis <= System.currentTimeMillis()) {
////                calendar.add(Calendar.WEEK_OF_YEAR, 1)
////            }
////
////            val alarmIntent = Intent(context, AlarmReceiver::class.java)
////            val pendingIntent = PendingIntent.getBroadcast(
////                context,
////                dayOfWeek,
////                alarmIntent,
////                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
////            )
////
////            alarmManager.setRepeating(
////                AlarmManager.RTC_WAKEUP,
////                calendar.timeInMillis,
////                AlarmManager.INTERVAL_DAY * 7,
////                pendingIntent
////            )
////
////            showAlert(title , calendar)
////
////        }
////    }
////
////    private fun addToFavorites(habit: String) {
////        val sharedPreferences = getSharedPreferences(com.example.habitwave.PREFS_NAME, Context.MODE_PRIVATE)
////        val favoritesSet =
////            sharedPreferences.getStringSet(com.example.habitwave.PREFS_KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
////        favoritesSet.add(habit)
////        with(sharedPreferences.edit()) {
////            putStringSet(com.example.habitwave.PREFS_KEY_FAVORITES, favoritesSet)
////            apply()
////        }
////    }
////}
////
////
////
////
