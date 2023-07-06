package com.example.todolist.presentation.ui.listOfToDo

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.domain.models.TodoItem

/**
 * Comparator - класс, который отвечает за сравнение элементов списка.
 */
class Comparator : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}