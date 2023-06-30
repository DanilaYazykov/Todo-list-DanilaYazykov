package com.example.todolist.data.dto

import com.example.todolist.domain.models.TodoItem

data class TodoResponseList(
    val list: List<TodoItem>,
)