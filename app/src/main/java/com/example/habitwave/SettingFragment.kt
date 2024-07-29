package com.example.habitwave

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.example.habitwave.databinding.FragmentSettingBinding


class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        val notificationSwitch = findPreference<SwitchPreferenceCompat>("notifications")
        notificationSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                enableNotification()
            } else {
                disableNotification()
            }
            true
        }
    }

//        val ringtonePreference = findPreference<RingtonePreference>("alarm_tone")
//        ringtonePreference?.setOnPreferenceChangeListener{preference,newValue ->
//            true
//        }


    //Handle notification enable/disable
//        val notificationPreference = findPreference<SwitchPreference>("notification")
//        notificationPreference?.setOnPreferenceChangeListener{ preference, newValue ->
//   if(newValue as Boolean){
//       enableNotification()
//   }
//            else{
//                disableNotification()
//            }
//            true
//        }
//    }
    private fun enableNotification() {
        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
            //Notification  are not eanbled , redirett ot settings

            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            startActivity(intent)
        } else {
            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


        }
    }
    private fun disableNotification() {

        removeNotification()
    }

    private fun  removeNotification(){
    val notificationManager:NotificationManager = requireContext().getSystemService(
        NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(resources.getColor(R.color.black, null))
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Your Title"
        }
        return view
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is RingtonePreference) {
            val dialogFragment = RingtonePreferenceDialogFragment.newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}
