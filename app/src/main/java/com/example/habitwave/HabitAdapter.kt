package com.example.habitwave

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitwave.databinding.ActivityHabitListBinding

class HabitAdapter(
    private val habitList: List<Habit>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HabitAdapter.MyHolder>() {

    interface OnItemClickListener {
        fun onItemClick(habit: Habit)
        fun onButtonClick(habit: Habit)
    }

    class MyHolder(val binding: ActivityHabitListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ActivityHabitListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val habit = habitList[position]
        holder.binding.txtHabitName.text = habit.title
        holder.binding.imgHabit.setImageResource(habit.imageId)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(habit)
        }

        holder.binding.btnHabitAdd.setOnClickListener {
            itemClickListener.onButtonClick(habit)
        }
    }
}
