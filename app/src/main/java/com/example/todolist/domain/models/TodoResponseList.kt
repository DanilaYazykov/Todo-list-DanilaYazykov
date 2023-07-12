package com.example.todolist.domain.models

import com.google.gson.annotations.SerializedName

data class TodoResponseList(
    @SerializedName("list")
    val list: List<TodoItem>,
    @SerializedName("revision")
    val revision: Int
)