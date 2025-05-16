//package com.example.habitwave
//
//import android.app.Application
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//
//class App : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Alarm Channel"
//            val descriptionText = "Channel for alarm notifications"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel("alarm_channel", name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}
