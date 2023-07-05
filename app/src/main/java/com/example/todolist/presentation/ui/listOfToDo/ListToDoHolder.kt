package com.example.todolist.presentation.ui.listOfToDo

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemOfToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.ui.api.OnCheckedClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListToDoHolder(itemView: View, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {
    private val binding = ItemOfToDoBinding.bind(itemView)

    fun bind(itemView: TodoItem, onCheckedChangeListener: OnCheckedClickListener) = with(binding) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val dateString = itemView.deadline?.let { Date(it) }?.let { dateFormat.format(it) }
        tvTodoTitle.text = itemView.text
        if (itemView.deadline != null) {
            tvDate.visibility = View.VISIBLE
            tvDate.text = dateString
        } else {
            tvDate.visibility = View.GONE
        }
        ivCheckbox.setOnClickListener {
            val updatedItem = itemView.copy(done = !itemView.done)
            setDone(checkBoxView = updatedItem.done)
            onCheckedChangeListener.onCheckedChange(updatedItem.id, updatedItem.done)
        }

        setDone(checkBoxView = itemView.done)
        if (!itemView.done) setPriority(itemView.importance)
    }

    private fun setPriority(itemView: TodoItem.Importance?) = with(binding) {
        if (itemView == null) return
        when (itemView) {
            TodoItem.Importance.LOW -> {
                ivPriority.visibility = View.VISIBLE
                ivPriority.setImageResource(R.drawable.ic_priority_low)
            }
            TodoItem.Importance.IMPORTANT -> {
                ivPriority.visibility = View.VISIBLE
                ivCheckbox.setImageResource(R.drawable.ic_checkbox_unchecked)
                ivPriority.setImageResource(R.drawable.ic_priority_high)
            }
            else -> {
                ivPriority.visibility = View.GONE
                ivCheckbox.setImageResource(R.drawable.ic_checkbox_empty)
            }
        }
    }

    private fun setDone(checkBoxView: Boolean) = with(binding) {
        if (checkBoxView) {
            ivCheckbox.setImageResource(R.drawable.ic_checkbox_checked)
            ivPriority.visibility = View.GONE
            tvTodoTitle.paintFlags = binding.tvTodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvTodoTitle.setTextColor(ContextCompat.getColor(context, R.color.grey))
        } else {
            ivCheckbox.setImageResource(R.drawable.ic_checkbox_empty)
            tvTodoTitle.paintFlags = binding.tvTodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            tvTodoTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }
}