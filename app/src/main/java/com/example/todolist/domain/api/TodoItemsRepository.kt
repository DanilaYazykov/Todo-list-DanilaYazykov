package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem

interface TodoItemsRepository {

    fun getTodoItems(): List<TodoItem>

    fun deleteTodoItem(todoItem: TodoItem)

    fun addTodoItem(todoItem: TodoItem)

    fun addDone(itemId: String, checked: Boolean)

}