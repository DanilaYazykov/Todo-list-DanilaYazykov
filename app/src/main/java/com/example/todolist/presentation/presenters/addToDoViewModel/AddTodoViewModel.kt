package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.dataBase.AppDatabase
import com.example.todolist.data.dataBase.models.toDeleted
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AddTodoViewModel - viewModel UI класса AddTodoFragment. Связывает слои Presentation и Domain.
 */
class AddTodoViewModel(
    private val database: AppDatabase
) : ViewModel() {


    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.getTodoDao().insertTodoItem(todoItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItem.let {
                database.getTodoDao().deleteTodoItem(todoItem)
                database.getDeletedItemDao().addToDeletedList(todoItem.toDeleted())
            }
        }
    }

   fun markAsNotSynced(todoItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemId.let {
                database.getTodoDao().markSynced(todoItemId, false)
            }
        }
   }
}