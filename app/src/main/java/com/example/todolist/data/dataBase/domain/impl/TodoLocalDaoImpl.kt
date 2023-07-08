package com.example.todolist.data.dataBase.domain.impl

import com.example.todolist.data.dataBase.domain.api.TodoLocalDao
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain класс. Содержит в себе бизнес-логику приложения.
 */
class TodoLocalDaoImpl @Inject constructor(
    private val todoDao: TodoLocalDao
) : TodoLocalDao {
    override suspend fun insertTodoItem(todoItem: TodoItem) {
        todoDao.insertTodoItem(todoItem)
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
}