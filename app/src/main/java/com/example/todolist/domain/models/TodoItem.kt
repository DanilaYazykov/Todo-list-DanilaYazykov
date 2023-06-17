package com.example.todolist.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: String?,
    val done: Boolean,
    val creationDate: String,
    val modificationDate: String?
) : Parcelable
