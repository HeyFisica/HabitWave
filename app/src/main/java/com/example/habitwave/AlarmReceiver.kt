package com.example.habitwave

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.PowerManager
import android.provider.MediaStore.Audio.Media
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.play.integrity.internal.m
import java.io.File


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null
        fun stopAlarm() {
            mediaPlayer.let {

                if (it != null) {
                    if (it.isPlaying) {
                        it?.stop()
                        it?.reset()
                        it?.release()
                        mediaPlayer = null

                    }
                }
            }


        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("HABIT_TITLE")
        Toast.makeText(context, "Alarm ringing for $title", Toast.LENGTH_LONG).show()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val alarmToneUriString = sharedPreferences.getString(
            "alarm_tone", "default_alarm"
        )
        val alarmToneUri =
            if (alarmToneUriString == "default_alarm" || alarmToneUriString.isNullOrEmpty()) {
//            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

                Uri.parse("android.resource://${context.packageName}/raw/iphone_15")
            } else {
                Uri.parse(alarmToneUriString)

            }

        mediaPlayer = MediaPlayer.create(context, alarmToneUri)
        if (mediaPlayer != null) {
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        } else {
            Toast.makeText(context, "Failed to play alarm tone", Toast.LENGTH_LONG).show()

        }

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "habitwave::AlarmWakeLock")
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
    }
}
