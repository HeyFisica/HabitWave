package com.example.habitwave


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SaveHabitActivity : AppCompatActivity() {

    private lateinit var saveForLaterListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val saveLaterHabits = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Habit List"
        setContentView(R.layout.activity_save_habit)
        showFavoritesFragment()


    }

    //
    private fun showFavoritesFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = FavoritesFragment()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }




}