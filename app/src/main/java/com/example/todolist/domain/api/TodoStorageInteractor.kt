package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoStorageInteractor {

    fun getTodoList(): Flow<List<TodoItem>>

    fun deleteTodoItem(todoItem: TodoItem)

    fun addTodoItem(todoItem: TodoItem)

    fun addDone(itemId: String, isChecked: Boolean)
}
