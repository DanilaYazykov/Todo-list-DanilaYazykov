package com.example.todolist.data.dataBase

import com.example.todolist.data.dataBase.dao.DeletedItemDao
import com.example.todolist.data.dataBase.dao.TodoLocalDao
import com.example.todolist.domain.dataBase.TodoLocalStorage
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Класс для взаимодействия с локальной базой данных(ROOM).
 */
class TodoLocalStorageImpl @Inject constructor(
    private val todoDao: TodoLocalDao,
    private val deletedItemDao: DeletedItemDao
) : TodoLocalStorage {
    override suspend fun insertTodoItem(todoItem: TodoItem) {
        todoDao.insertTodoItem(todoItem)
    }

    override suspend fun insertListTodoItem(todoItem: List<TodoItem>) {
        todoDao.insertListTodoItem(todoItem)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        todoDao.updateTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoDao.deleteTodoItem(todoItem)
    }

    override fun getAllTodoItems(): Flow<List<TodoItem>> {
        return todoDao.getAllTodoItems()
    }

    override suspend fun deleteAllTodoItems() {
        todoDao.deleteAllTodoItems()
    }

    override suspend fun updateCurrentItemDone(id: String, isChecked: Boolean, modification: Long) {
        todoDao.updateCurrentItemDone(id, isChecked, modification)
    }

    override suspend fun getUnsyncedItems(): List<TodoItem> {
        return todoDao.getUnsyncedItems()
    }

    override suspend fun markSynced(id: String, synced: Boolean) {
        todoDao.markSynced(id, synced)
    }

    override suspend fun addToDeletedList(deletedItem: DeletedItemsEntity) {
        deletedItemDao.addToDeletedList(deletedItem)
    }

    override suspend fun getDeletedItems(): List<DeletedItemsEntity> {
        return deletedItemDao.getDeletedItems()
    }

    override suspend fun deleteAllDeletedItems() {
        deletedItemDao.deleteAllDeletedItems()
    }
}