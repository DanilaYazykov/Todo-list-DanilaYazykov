package com.example.todolist.presentation.ui.api

import com.example.todolist.domain.models.TodoItem

/**
 * OnItemClickListener - интерфейс, который отвечает за обработку нажатия на элемент списка.
 */
interface OnItemClickListener {
    fun onItemClick(todo: TodoItem)
}