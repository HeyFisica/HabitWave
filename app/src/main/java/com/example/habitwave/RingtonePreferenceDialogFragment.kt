package com.example.habitwave

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat

class RingtonePreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    private var ringtonePreference: RingtonePreference? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        ringtonePreference = preference as RingtonePreference
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtonePreference?.ringtoneUri)
        startActivityForResult(intent, 0)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        // No-op
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 0) {
            val uri: Uri? = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (uri != null) {
                ringtonePreference?.ringtoneUri = uri.toString()
                ringtonePreference?.summary = RingtoneManager.getRingtone(context, uri)?.getTitle(context)
            }
            else{
                ringtonePreference?.ringtoneUri = "default_alarm"
                ringtonePreference?.summary = "Default Alarm Tone"
            }
        }
    }
    //practice code


    //end of practice code
    companion object {
        fun newInstance(key: String): RingtonePreferenceDialogFragment {
            val fragment = RingtonePreferenceDialogFragment()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }

        private const val ARG_KEY = "key"
    }
}

//
