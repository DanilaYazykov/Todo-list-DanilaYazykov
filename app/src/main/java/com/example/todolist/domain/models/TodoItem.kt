package com.example.todolist.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    var id: String,
    var text: String,
    var importance: String,
    var deadline: String?,
    var done: Boolean,
    var creationDate: String,
    var modificationDate: String?
) : Parcelable
