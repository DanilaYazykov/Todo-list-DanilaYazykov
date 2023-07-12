package com.example.todolist.domain.models

data class TodoPostList(
    val status: String,
    val list: List<TodoItem>
)
