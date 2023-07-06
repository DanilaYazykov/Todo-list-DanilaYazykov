package com.example.todolist.data.sharedPreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * Слой Data.
 * Класс который отвечает за работу с локальным хранилищем.
 */
class TodoLocalStorageImpl(context: Context) : TodoStorageInteractor {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(TODO_PREFS, MODE_PRIVATE)

    override fun getTodoList(): Flow<List<TodoItem>> = flow {
        val savedTodoJson = sharedPrefs.getString(TODO_ITEMS, null)
        if (!savedTodoJson.isNullOrEmpty()) {
            val savedTodo = Gson().fromJson(savedTodoJson, Array<TodoItem>::class.java)
            emit(savedTodo.toList())
        } else {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            val savedTodo = getFromJson()
            val existingItem = savedTodo.find { it.id == todoItem.id }
            if (existingItem != null) {
                savedTodo.remove(existingItem)
            }
            todoJson(savedTodo)
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            val savedTodo = getFromJson()
            val existingItemIndex = savedTodo.indexOfFirst { it.id == todoItem.id }
            if (existingItemIndex != -1) {
                savedTodo[existingItemIndex] = todoItem
            } else {
                savedTodo.add(todoItem)
            }
            todoJson(savedTodo)
        }
    }

    override suspend fun addDone(itemId: String, isChecked: Boolean) {
         withContext(Dispatchers.IO) {
            val savedTodo = getFromJson()
            val existingItemIndex = savedTodo.indexOfFirst { it.id == itemId }
            if (existingItemIndex != -1) {
                val existingItem = savedTodo[existingItemIndex]
                val updatedItem = existingItem
                    .copy(done = isChecked, modificationDate = Calendar.getInstance().timeInMillis)
                savedTodo[existingItemIndex] = updatedItem
            }
            todoJson(savedTodo)
        }
    }

    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            sharedPrefs.edit()
                .clear()
                .apply()
        }
    }

    override suspend fun getUnsyncedItems(): List<TodoItem> {
        val savedTodo = getFromJson()
        return savedTodo.filter { !it.synced }
    }

    override suspend fun markAsSynced(id: String) {
        val savedTodo = getFromJson()
        val existingItemIndex = savedTodo.indexOfFirst { res -> res.id == id }
        if (existingItemIndex != -1) {
            val existingItem = savedTodo[existingItemIndex]
            val updatedItem = existingItem.copy(synced = true)
            savedTodo[existingItemIndex] = updatedItem
            todoJson(savedTodo)
        }
    }

    override suspend fun markAsNotSynced(id: String) {
        withContext(Dispatchers.IO) {
        val savedTodo = getFromJson()
        val existingItemIndex = savedTodo.indexOfFirst { res -> res.id == id }
        if (existingItemIndex != -1) {
            val existingItem = savedTodo[existingItemIndex]
            val updatedItem = existingItem.copy(synced = false)
            savedTodo[existingItemIndex] = updatedItem
            todoJson(savedTodo)
        }
        }
    }

    override suspend fun addToDeletedList(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            val deletedItems = getDeletedItems().toMutableList()
            deletedItems.add(todoItem)
            setDeletedItems(deletedItems)
        }
    }

    override suspend fun getDeletedItems(): List<TodoItem> {
        val deletedItemsJson = sharedPrefs.getString(DELETED_LIST, null)
        return if (!deletedItemsJson.isNullOrEmpty()) {
            Gson().fromJson(deletedItemsJson, Array<TodoItem>::class.java).toList()
        } else {
            emptyList()
        }
    }

    private fun setDeletedItems(deletedItems: List<TodoItem>) {
        val deletedItemsJson = Gson().toJson(deletedItems)
        sharedPrefs.edit()
            .putString(DELETED_LIST, deletedItemsJson)
            .apply()
    }

    private fun todoJson(savedTodo: List<TodoItem>) {
        val todoJson = Gson().toJson(savedTodo)
        sharedPrefs.edit()
            .putString(TODO_ITEMS, todoJson)
            .apply()
    }

    private fun getFromJson(): MutableList<TodoItem> {
        val savedTodoJson = sharedPrefs.getString(TODO_ITEMS, null)
        val savedTodo = if (!savedTodoJson.isNullOrEmpty()) {
            Gson().fromJson(savedTodoJson, Array<TodoItem>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
        return savedTodo
    }

    companion object {
        private const val TODO_PREFS = "todoPreferencesNames"
        private const val TODO_ITEMS = "todoItems"
        private const val DELETED_LIST = "deletedList"
    }
}