package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.dataBase.converters.DeleteItemDbConverter
//import com.example.todolist.data.dataBase.toDeleted
import com.example.todolist.domain.dataBase.TodoLocalInteractor
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AddTodoViewModel - viewModel UI класса AddTodoFragment. Связывает слои Presentation и Domain.
 */
class AddTodoViewModel(
    private val database: TodoLocalInteractor,
    private val converter: DeleteItemDbConverter
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
                database.addToDeletedList(converter.map(todoItem))
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