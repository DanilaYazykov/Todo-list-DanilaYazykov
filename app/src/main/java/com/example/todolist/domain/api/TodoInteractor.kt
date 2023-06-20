package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoItem

interface TodoInteractor {

    fun getTodoList(todoInfoConsumer: TodoInfoConsumer)

    fun deleteTodoItem(todoItem: TodoItem)

    fun addTodoItem(todoItem: TodoItem)

    fun addDone(itemId: String, isChecked: Boolean)

    interface TodoInfoConsumer {
        fun onTodoInfoReceived(todoInfo: List<TodoItem>)
    }
}
