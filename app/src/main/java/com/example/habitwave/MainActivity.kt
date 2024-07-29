

package com.example.habitwave

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitwave.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), HabitAdapter.OnItemClickListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Add Habit"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNotificationPermission()

        // Initialize the habit list with image resource IDs
        val habitList: ArrayList<Habit> = ArrayList()
        habitList.add(Habit("Meditation", R.drawable.meditation))
        habitList.add(Habit("Yoga", R.drawable.yoga))
        habitList.add(Habit("Exercise", R.drawable.exercise))
        habitList.add(Habit("Workout", R.drawable.workout))
        habitList.add(Habit("Journal", R.drawable.journal))
        habitList.add(Habit("Guitar", R.drawable.gitar))
        habitList.add(Habit("Eating", R.drawable.eating))
        habitList.add(Habit("Reading", R.drawable.reading))
        habitList.add(Habit("Skincare", R.drawable.skincare))
        habitList.add(Habit("Sleeping", R.drawable.sleeping))

        // Set up RecyclerView with a linear layout manager and adapter
        binding.habitRecyclerView.layoutManager = LinearLayoutManager(this)
        val habitAdapter = HabitAdapter(habitList, this)
        binding.habitRecyclerView.adapter = habitAdapter
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // Permission granted, continue with notification setup
//                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onItemClick(habit: Habit) {
        Toast.makeText(this, "Item clicked: ${habit.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onButtonClick(habit: Habit) {
        val intent = Intent(this, AddAlarmActivity::class.java).apply {
            putExtra("HABIT_TITLE", habit.title)
        }
        startActivity(intent)
    }
}
