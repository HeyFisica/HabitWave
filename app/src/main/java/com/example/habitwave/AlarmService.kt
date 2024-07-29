package com.example.habitwave

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.nfc.Tag
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmService : Service() {


companion object{
    private  const val TAG = "AlarmService"
}
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val action = intent?.action
            Log.d(TAG,"onStartCommand start with action : $action")
            if (action == "STOP_ALARM") {
                // Handle the stop command
                stopSelf()
                // Add any additional cleanup or stopping code here
                Log.d("AlarmService", "Alarm stopped")
            } else {
                // Handle other actions or start the alarm
                Log.d("AlarmService", "Alarm started")
            }
            return START_NOT_STICKY
        }

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }
    }


