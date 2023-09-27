package com.example.todolist.presentation.viewModels.addToDoViewModel

import com.example.todolist.domain.models.TodoItem

sealed interface AddTodoEvent

class AddTodoItemEvent(val todoItem: TodoItem) : AddTodoEvent

class DeleteTodoItemEvent(val todoItem: TodoItem) : AddTodoEvent

class MarkAsNotSyncedEvent(val todoItemId: String) : AddTodoEvent
