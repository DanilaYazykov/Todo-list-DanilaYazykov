package com.example.todolist.data.dto

import com.example.todolist.domain.models.TodoItem

data class TodoPostList(
    val status: String,
    val list: List<TodoItem>
)
