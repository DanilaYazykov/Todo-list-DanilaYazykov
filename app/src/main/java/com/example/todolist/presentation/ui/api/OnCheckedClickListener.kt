package com.example.todolist.presentation.ui.api


interface OnCheckedClickListener {
    fun onCheckedChange(todoItem: String, isChecked: Boolean)
}