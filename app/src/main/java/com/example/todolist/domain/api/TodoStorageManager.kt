package com.example.todolist.domain.api

import com.example.todolist.data.sharedPreferences.TodoStorageManagerImpl
import com.example.todolist.domain.models.TodoItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface TodoStorageManager {

    fun getTodoItems(): Flow<List<TodoItem>>

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun addDone(itemId: String, checked: Boolean)

    suspend fun clearAll()

    suspend fun getUnsyncedItems(): List<TodoItem>

    suspend fun markAsSynced(id: String)

    suspend fun markAsNotSynced(id: String)

    suspend fun addToDeletedList(todoItem: TodoItem)

    suspend fun getDeletedItems(): List<TodoItem>
}