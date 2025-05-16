package com.example.habitwave

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

const val PREFS_NAME = "habit_prefs"
const val PREFS_KEY_FAVORITES = "favorites"


class FavoritesFragment : Fragment() {

    private lateinit var favoritesListView: ListView
    private lateinit var favoritesAdapter: ArrayAdapter<String>
    private val favoriteHabits = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.habitwave.R.layout.fragment_favorites, container, false)
        favoritesListView = view.findViewById(com.example.habitwave.R.id.favoritesListView)
        favoritesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, favoriteHabits)
        favoritesListView.adapter = favoritesAdapter

        loadFavorites()

        favoritesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedHabit = favoriteHabits[position]
            showDeleteConfirmationDialog(selectedHabit, position)
        }

        return view
    }

    private fun loadFavorites() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, emptySet()) ?: emptySet()
        favoriteHabits.clear()
        favoriteHabits.addAll(favoritesSet)
        favoritesAdapter.notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(habit: String, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete this habit?")
            .setPositiveButton("Yes") { _, _ ->
                deleteHabit(habit, position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteHabit(habit: String, position: Int) {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet = sharedPreferences.getStringSet(PREFS_KEY_FAVORITES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        favoritesSet.remove(habit)
        with(sharedPreferences.edit()) {
            putStringSet(PREFS_KEY_FAVORITES, favoritesSet)
            apply()
        }
        favoriteHabits.removeAt(position)
        favoritesAdapter.notifyDataSetChanged()

        cancelAlarm(habit)
    }


    private fun cancelAlarm(habit: String) {
        val alarmParts = habit.split(" - ")
        if (alarmParts.size == 4) {
            val timeParts = alarmParts[2].split(":")

            // Check if alarmParts[3] is not empty
            if (alarmParts[3].isNotEmpty()) {
                val daysOfWeek = alarmParts[3].split(",").mapNotNull { it.toIntOrNull() }

                val hour = timeParts[0].toIntOrNull()
                val minute = timeParts[1].toIntOrNull()

                if (hour != null && minute != null) {
                    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    daysOfWeek.forEach { dayOfWeek ->
                        val intent = Intent(requireContext(), AlarmReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            dayOfWeek,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        alarmManager.cancel(pendingIntent)
                    }
                }
            }
        }
    }

}
