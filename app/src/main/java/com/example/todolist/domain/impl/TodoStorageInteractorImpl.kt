package com.example.todolist.domain.impl

import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

/**
 * Domain класс. Содержит в себе бизнес-логику приложения.
 */
class TodoStorageInteractorImpl(private val todoItemsRepository: TodoStorageInteractor) : TodoStorageInteractor {

    override fun getTodoList(): Flow<List<TodoItem>> {
        return todoItemsRepository.getTodoList()
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

    override suspend fun getUnsyncedItems(): List<TodoItem> {
        return todoItemsRepository.getUnsyncedItems()
    }

    override suspend fun markAsSynced(id: String) {
        todoItemsRepository.markAsSynced(id)
    }

    override suspend fun markAsNotSynced(id: String) {
        todoItemsRepository.markAsNotSynced(id)
    }

    override suspend fun addToDeletedList(todoItem: TodoItem) {
        todoItemsRepository.addToDeletedList(todoItem)
    }

    override suspend fun getDeletedItems(): List<TodoItem> {
        return todoItemsRepository.getDeletedItems()
    }
}