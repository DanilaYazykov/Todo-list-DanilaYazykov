package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoStorageInteractor {

    fun getTodoList(): Flow<List<TodoItem>>

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun addDone(itemId: String, isChecked: Boolean)

    suspend fun clearAll()
}
