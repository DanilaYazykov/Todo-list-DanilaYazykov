package com.example.todolist.data.sharedPreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.todolist.domain.api.TodoStorageManager
import com.example.todolist.domain.models.TodoItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Calendar

class TodoStorageManagerImpl(context: Context) : TodoStorageManager {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(TODO_PREFS, MODE_PRIVATE)

    override fun getTodoItems(): Flow<List<TodoItem>> = flow {
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
                val updatedItem = savedTodo[existingItemIndex].copy(
                    text = todoItem.text,
                    importance = todoItem.importance,
                    creationDate = todoItem.creationDate,
                    deadline = todoItem.deadline,
                    done = todoItem.done,
                    modificationDate = todoItem.modificationDate,
                    color = todoItem.color,
                    lastUpdatedBy = todoItem.lastUpdatedBy
                )
                savedTodo[existingItemIndex] = updatedItem
            } else {
                savedTodo.add(todoItem)
            }
            todoJson(savedTodo)
        }
    }

    override suspend fun addDone(itemId: String, checked: Boolean) {
         withContext(Dispatchers.IO) {
            val savedTodo = getFromJson()
            val existingItemIndex = savedTodo.indexOfFirst { it.id == itemId }
            if (existingItemIndex != -1) {
                val existingItem = savedTodo[existingItemIndex]
                val updatedItem = existingItem.copy(done = checked, modificationDate = Calendar.getInstance().timeInMillis)
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
    }
}