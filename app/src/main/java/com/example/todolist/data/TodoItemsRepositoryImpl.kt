package com.example.todolist.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.todolist.domain.api.TodoItemsRepository
import com.example.todolist.domain.models.TodoItem
import com.google.gson.Gson

/**
 * Я решил отказаться от моков и сделать реальную реализацию репозитория с сохранением в SharedPreferences
 * В дальнейшем может пригодится в случае отсутствия интернета у пользователя ->
 * -> сохраняем локально, при появлении интернета отправляем на сервер
 */

class TodoItemsRepositoryImpl(context: Context) : TodoItemsRepository {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(TODO_PREFS, MODE_PRIVATE)

    override fun getTodoItems(): List<TodoItem> {
        val savedTracksJson = sharedPrefs.getString(TODO_ITEMS, null)
        return if (!savedTracksJson.isNullOrEmpty()) {
            val savedTracks = Gson().fromJson(savedTracksJson, Array<TodoItem>::class.java)
            savedTracks.toList()
        } else {
            emptyList()
        }
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        /**
         * найди в списке сохраненных треков трек с таким же id и удалить его
         * сохранить обновленный список в sharedPrefs
         *
         */
        val savedTracksJson = sharedPrefs.getString(TODO_ITEMS, null)
        val savedTracks = if (!savedTracksJson.isNullOrEmpty()) {
            Gson().fromJson(savedTracksJson, Array<TodoItem>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        val existingItem = savedTracks.find { it.id == todoItem.id }
        if (existingItem != null) {
            savedTracks.remove(existingItem)
        }

        val tracksJson = Gson().toJson(savedTracks)
        sharedPrefs.edit()
            .putString(TODO_ITEMS, tracksJson)
            .apply()
    }

    override fun addTodoItem(todoItem: TodoItem) {
        val savedTracksJson = sharedPrefs.getString(TODO_ITEMS, null)
        val savedTracks = if (!savedTracksJson.isNullOrEmpty()) {
            Gson().fromJson(savedTracksJson, Array<TodoItem>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        val existingItem = savedTracks.find { it.id == todoItem.id }
        if (existingItem != null) {
            existingItem.text = todoItem.text
            existingItem.importance = todoItem.importance
            existingItem.deadline = todoItem.deadline
            existingItem.modificationDate = todoItem.modificationDate
        } else {
            savedTracks.add(todoItem)
        }

        val tracksJson = Gson().toJson(savedTracks)
        sharedPrefs.edit()
            .putString(TODO_ITEMS, tracksJson)
            .apply()
    }

    override fun addDone(itemId: String, checked: Boolean) {
        val savedTracksJson = sharedPrefs.getString(TODO_ITEMS, null)
        val savedTracks = if (!savedTracksJson.isNullOrEmpty()) {
            Gson().fromJson(savedTracksJson, Array<TodoItem>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        val existingItem = savedTracks.find { it.id == itemId }
        if (existingItem != null) {
            existingItem.done = checked
        }

        val tracksJson = Gson().toJson(savedTracks)
        sharedPrefs.edit()
            .putString(TODO_ITEMS, tracksJson)
            .apply()
    }

    companion object {
        private const val TODO_PREFS = "todoPrefsNames"
        private const val TODO_ITEMS = "todoItems"
    }
}