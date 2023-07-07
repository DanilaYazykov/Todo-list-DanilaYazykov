package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem
import dagger.Provides
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс, который связывает слои Presentation и Domain.
 * Ввиду того, что имеет одинаковые методы, то также является интерфейсом для слоя Data.
 */

interface TodoStorageInteractor {

    fun getTodoList(): Flow<List<TodoItem>>

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun addDone(itemId: String, isChecked: Boolean)

    suspend fun clearAll()

    suspend fun getUnsyncedItems(): List<TodoItem>

    suspend fun markAsSynced(id: String)

    suspend fun markAsNotSynced(id: String)

    suspend fun addToDeletedList(todoItem: TodoItem)

    suspend fun getDeletedItems(): List<TodoItem>

}
