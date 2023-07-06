package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.launch

/**
 * AddTodoViewModel - viewModel UI класса AddTodoFragment. Связывает слои Presentation и Domain.
 */
class AddTodoViewModel(private val todoInteractor: TodoStorageInteractor) : ViewModel() {

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoInteractor.addTodoItem(todoItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItem.let {
                todoInteractor.deleteTodoItem(todoItem)
                todoInteractor.addToDeletedList(todoItem)
            }
        }
    }

   fun markAsNotSynced(todoItemId: String) {
        viewModelScope.launch {
            todoItemId.let {
                todoInteractor.markAsNotSynced(todoItemId)
            }
        }
   }
}