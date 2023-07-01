package com.example.todolist.domain.impl

import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.api.TodoStorageManager
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoStorageInteractorImpl(private val todoItemsRepository: TodoStorageManager) : TodoStorageInteractor {

    override fun getTodoList(): Flow<List<TodoItem>> {
        return todoItemsRepository.getTodoItems()
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsRepository.deleteTodoItem(todoItem)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemsRepository.addTodoItem(todoItem)
    }

    override suspend fun addDone(itemId: String, isChecked: Boolean) {
        todoItemsRepository.addDone(itemId, isChecked)
    }

    override suspend fun clearAll() {
        todoItemsRepository.clearAll()
    }
}