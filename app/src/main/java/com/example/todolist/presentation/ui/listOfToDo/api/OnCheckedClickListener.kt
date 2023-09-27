package com.example.todolist.presentation.ui.listOfToDo.api

/**
 * OnCheckedClickListener - интерфейс, который отвечает за обработку нажатия на чекбокс.
 */
interface OnCheckedClickListener {
    fun onCheckedChange(todoItem: String, isChecked: Boolean)
}