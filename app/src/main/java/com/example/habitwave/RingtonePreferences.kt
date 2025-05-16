package com.example.habitwave

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.bumptech.glide.Glide.init

class RingtonePreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {
//
//    var ringtoneUri: Uri? = null
//        set(value) {
//            field = value
//            persistString(value?.toString())
//        }
//
//    init {
//        summaryProvider = SummaryProvider<RingtonePreference> { preference ->
//            val uriString = preference.getPersistedString(null)
//            if (uriString != null) {
//                val uri = Uri.parse(uriString)
//                RingtoneManager.getRingtone(context, uri)?.getTitle(context) ?: "Default"
//            } else {
//                "Default"
//            }
//        }
//
//}
//
//    override fun getDialogLayoutResource(): Int {
//        return 0 // Use default layout for dialog
//    }
//
//    override fun onAttached() {
//        super.onAttached()
//        val uriString = getPersistedString(null)
//        if (uriString != null) {
//            ringtoneUri = Uri.parse(uriString)
//        }
//    }
//}
//
//

//chekcode

    var ringtoneUri: String?
        get() = getPersistedString(null)
        set(value) {
            persistString(value)
            summary = if(value == "default_alarm"){
                "Default Alarm Tone"
            }
            else {
                RingtoneManager.getRingtone(context, Uri.parse(value))?.getTitle(context)
            }

        }

    init {
        // Remove any default SummaryProvider
        summaryProvider = null
    }

    override fun onAttached() {
        super.onAttached()
        // Ensure summary is set correctly when the preference is attached
        ringtoneUri?.let {
            summary = if(it == "default_alarm"){
                "Default Alarm Tone"
            }
            else{
                RingtoneManager.getRingtone(context, Uri.parse(it))?.getTitle(context)

            }
        }
    }
}