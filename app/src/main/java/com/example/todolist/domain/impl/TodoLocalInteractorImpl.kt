package com.example.todolist.domain.impl

import com.example.todolist.data.dataBase.DeletedItemsEntity
import com.example.todolist.domain.dataBase.TodoLocalStorage
import com.example.todolist.domain.dataBase.TodoLocalInteractor
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoLocalInteractorImpl @Inject constructor
    (private val todoLocal: TodoLocalStorage) : TodoLocalInteractor {
    override suspend fun insertTodoItem(todoItem: TodoItem) {
        todoLocal.insertTodoItem(todoItem)
    }

    override suspend fun insertListTodoItem(todoItem: List<TodoItem>) {
        todoLocal.insertListTodoItem(todoItem)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        todoLocal.updateTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoLocal.deleteTodoItem(todoItem)
    }

    override fun getAllTodoItems(): Flow<List<TodoItem>> {
        return todoLocal.getAllTodoItems()
    }

    override suspend fun deleteAllTodoItems() {
        todoLocal.deleteAllTodoItems()
    }

    override suspend fun updateCurrentItemDone(id: String, isChecked: Boolean, modification: Long) {
        todoLocal.updateCurrentItemDone(id, isChecked, modification)
    }

    override suspend fun getUnsyncedItems(): List<TodoItem> {
        return todoLocal.getUnsyncedItems()
    }

    override suspend fun markSynced(id: String, synced: Boolean) {
        todoLocal.markSynced(id, synced)
    }

    override suspend fun addToDeletedList(deletedItem: DeletedItemsEntity) {
        todoLocal.addToDeletedList(deletedItem)
    }

    override suspend fun getDeletedItems(): List<DeletedItemsEntity> {
        return todoLocal.getDeletedItems()
    }

    override suspend fun deleteAllDeletedItems() {
        todoLocal.deleteAllDeletedItems()
    }
}