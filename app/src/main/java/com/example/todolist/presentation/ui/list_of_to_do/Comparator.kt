package com.example.todolist.presentation.ui.list_of_to_do

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.domain.models.TodoItem

class Comparator : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}