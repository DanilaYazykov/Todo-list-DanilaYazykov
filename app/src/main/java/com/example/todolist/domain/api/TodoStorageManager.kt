package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoStorageManager {

    fun getTodoItems(): Flow<List<TodoItem>>

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun addDone(itemId: String, checked: Boolean)

    suspend fun clearAll()

}