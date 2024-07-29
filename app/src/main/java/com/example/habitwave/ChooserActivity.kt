package com.example.habitwave

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.habitwave.databinding.ActivityChooserBinding

class ChooserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooserBinding.inflate(layoutInflater)
       setContentView(binding.root)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)

        binding.addNewHabit.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)


        }

        binding.removeHabitBtn.setOnClickListener {
            val intent = Intent(this, SaveHabitActivity::class.java)
            startActivity(intent)
        }

        binding.chooseHabitBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.CheckSavedBtn.setOnClickListener {
            startActivity(Intent(this, SaveHabitActivity::class.java))
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentTransaction(HomeFragment())
                    true
                }

                R.id.person -> {
                    fragmentTransaction(PersonFragment())
                    true
                }

                R.id.setting -> {
                    fragmentTransaction(SettingFragment())
                    true
                }

                else -> false
            }
        }

        // Set the default fragment
        fragmentTransaction(HomeFragment())


    }

    private fun fragmentTransaction(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Handle orientation changes
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Do something when the orientation is landscape
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Do something when the orientation is portrait
        }
    }

}

