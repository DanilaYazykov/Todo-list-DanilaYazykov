package com.example.todolist.presentation.ui.api

import com.example.todolist.domain.models.TodoItem

interface OnItemClickListener {
    fun onItemClick(todo: TodoItem)
}