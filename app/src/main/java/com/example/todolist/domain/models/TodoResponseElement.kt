package com.example.todolist.domain.models

import com.google.gson.annotations.SerializedName

data class TodoResponseElement(
    @SerializedName("element")
    val element: TodoItem,
    @SerializedName("revision")
    val revision: String,
    @SerializedName("status")
    val status: String
)