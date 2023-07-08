package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.dataBase.domain.impl.DeletedItemDaoImpl
import com.example.todolist.data.dataBase.domain.impl.TodoLocalDaoImpl
import com.example.todolist.data.dataBase.models.toDeleted
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AddTodoViewModel - viewModel UI класса AddTodoFragment. Связывает слои Presentation и Domain.
 */
class AddTodoViewModel(
    private val database: TodoLocalDaoImpl,
    private val databaseOffline: DeletedItemDaoImpl,
) : ViewModel() {


    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.insertTodoItem(todoItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItem.let {
                database.deleteTodoItem(todoItem)
                databaseOffline.addToDeletedList(todoItem.toDeleted())
            }
        }
    }

   fun markAsNotSynced(todoItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemId.let {
                database.markSynced(todoItemId, false)
            }
        }
   }
}