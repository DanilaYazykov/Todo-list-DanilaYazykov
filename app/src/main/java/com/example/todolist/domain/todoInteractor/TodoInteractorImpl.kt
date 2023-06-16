package com.example.todolist.domain.todoInteractor

import com.example.todolist.domain.api.TodoInteractor
import com.example.todolist.domain.api.TodoItemsRepository
import com.example.todolist.domain.models.TodoItem

class TodoInteractorImpl(private val todoItemsRepository: TodoItemsRepository) : TodoInteractor {

    override fun getTodoList(todoInfoConsumer: TodoInteractor.TodoInfoConsumer) {
        todoInfoConsumer.onTodoInfoReceived(todoItemsRepository.getTodoItems())
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsRepository.deleteTodoItem(todoItem)
    }

    override fun addTodoItem(todoItem: TodoItem) {
        todoItemsRepository.addTodoItem(todoItem)

    }

    override fun addDone(itemId: String, isChecked: Boolean) {
        todoItemsRepository.addDone(itemId, isChecked)
    }
}