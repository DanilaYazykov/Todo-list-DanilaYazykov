package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem

class AddTodoViewModel(private val todoInteractor: TodoStorageInteractor) : ViewModel() {

    fun addTodoItem(todoItem: TodoItem) {
        todoInteractor.addTodoItem(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItem.let {
            todoInteractor.deleteTodoItem(todoItem)
        }
    }

}