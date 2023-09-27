package com.example.todolist.presentation.viewModels.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.dataBase.converters.DeleteItemDbConverter
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

    fun action(event: AddTodoEvent) {
        when (event) {
            is AddTodoItemEvent -> addTodoItem(event.todoItem)
            is DeleteTodoItemEvent -> deleteTodoItem(event.todoItem)
            is MarkAsNotSyncedEvent -> markAsNotSynced(event.todoItemId)
        }
    }

    private fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.insertTodoItem(todoItem)
        }
    }

    private fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItem.let {
                database.deleteTodoItem(todoItem)
                database.addToDeletedList(converter.map(todoItem))
            }
        }
    }

    private fun markAsNotSynced(todoItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemId.let {
                database.markSynced(todoItemId, false)
            }
        }
   }
}