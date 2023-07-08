package com.example.todolist.data.dataBase.impl

import com.example.todolist.data.dataBase.api.DeletedItemDao
import com.example.todolist.data.dataBase.models.DeletedItems
import com.example.todolist.data.dataBase.api.TodoLocalDao
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Класс, реализующий интерфейс [TodoLocalDao] и [DeletedItemDao].
 * Отвечает за работу с локальной базой данных.
 */
class TodoLocalStorageImpl @Inject constructor(
    private val todoDao: TodoLocalDao,
    private val deletedItemDao: DeletedItemDao
) : TodoLocalDao, DeletedItemDao {

    override suspend fun insertTodoItem(todoItem: TodoItem) {
        todoDao.insertTodoItem(todoItem)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        todoDao.updateTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoDao.deleteTodoItem(todoItem)
    }

    override fun getAllTodoItems(): Flow<List<TodoItem>> = flow {
        todoDao.getAllTodoItems()
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

    override suspend fun addToDeletedList(deletedItem: DeletedItems) {
        deletedItemDao.addToDeletedList(deletedItem)
    }

    override suspend fun getDeletedItems(): List<DeletedItems> {
        return deletedItemDao.getDeletedItems()
    }
}