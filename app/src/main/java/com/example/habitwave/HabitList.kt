package com.example.habitwave

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitwave.databinding.ActivityHabitListBinding
class HabitList : AppCompatActivity() {

//class HabitList : AppCompatActivity(), HabitAdapter.OnItemClickListener {
    private lateinit var binding: ActivityHabitListBinding
    private lateinit var habitAdapter: HabitAdapter
//    private val habitList = arrayListOf(
//        Habit("Exercise", "Morning exercise for 30 minutes"),
//        Habit("Read", "Read a book for 1 hour"),
//        Habit("Meditate", "Meditate for 15 minutes")
//    )
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        // Initialize the RecyclerView
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        habitAdapter = HabitAdapter(habitList, this)
//        binding.recyclerView.adapter = habitAdapter
//
//        // Handle Add button click
//        binding.add_button.setOnClickListener {
//            addNewHabit()
//        }
//    }
//
//    private fun addNewHabit() {
//        val newHabit = Habit("New Habit", "Description of new habit")
//        habitList.add(newHabit)
//        habitAdapter.notifyItemInserted(habitList.size - 1)
//        binding.recyclerView.scrollToPosition(habitList.size - 1)
//    }
//
//    override fun onItemClick(habit: Habit) {
//        Toast.makeText(this, "Clicked: ${habit.title}", Toast.LENGTH_SHORT).show()
    }
}
