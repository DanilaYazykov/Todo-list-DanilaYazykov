package com.example.todolist.presentation.viewModels.listOfToDoViewModel

interface ListOfTodoEvent

class LoadTodoListEvent : ListOfTodoEvent

class SyncTodoListFromNetworkEvent : ListOfTodoEvent

class UpdateDataServerEvent : ListOfTodoEvent

class ChangeVisibilityEvent : ListOfTodoEvent

class AddDoneEvent(val itemId: String, val isChecked: Boolean) : ListOfTodoEvent

class CheckNetworkEvent : ListOfTodoEvent

class ResetSyncFlagEvent : ListOfTodoEvent