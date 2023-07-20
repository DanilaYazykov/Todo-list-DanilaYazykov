package com.example.todolist.domain.dataBase

import com.example.todolist.data.dataBase.DeletedItemsEntity
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoLocalInteractor {

    suspend fun insertTodoItem(todoItem: TodoItem)

    suspend fun insertListTodoItem(todoItem: List<TodoItem>)

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    fun getAllTodoItems(): Flow<List<TodoItem>>

    suspend fun deleteAllTodoItems()

    suspend fun updateCurrentItemDone(id: String, isChecked: Boolean, modification: Long)

    suspend fun getUnsyncedItems(): List<TodoItem>

    suspend fun markSynced(id: String, synced: Boolean)

    suspend fun addToDeletedList(deletedItem: DeletedItemsEntity)

    suspend fun getDeletedItems(): List<DeletedItemsEntity>

    suspend fun deleteAllDeletedItems()

}