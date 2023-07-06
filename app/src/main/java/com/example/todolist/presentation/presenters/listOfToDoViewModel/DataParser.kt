package com.example.todolist.presentation.presenters.listOfToDoViewModel

import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.domain.models.TodoResponseList

/**
 * DataParser - класс, который отвечает за синхронизацию полученных данных и выдачу результата.
 */
class DataParser {

    suspend fun parseData(
        result: Pair<NetworkResult, TodoResponseList>,
        todoInteractor: TodoStorageInteractor,
        localList: List<TodoItem>,
        hideDoneItems: Boolean
    ): Pair<NetworkResult, List<TodoItem>> {
        return syncronizingLocalAndNetwork(result, todoInteractor, localList, hideDoneItems)
    }

    private suspend fun syncronizingLocalAndNetwork(
        result: Pair<NetworkResult, TodoResponseList>,
        todoInteractor: TodoStorageInteractor,
        localList: List<TodoItem>,
        hideDoneItems: Boolean
    ): Pair<NetworkResult, List<TodoItem>> {
        return when (result.first) {
            NetworkResult.SUCCESS_200 -> {
                successData(result, todoInteractor, localList, hideDoneItems)
            }
            else -> Pair(result.first, emptyList())
        }
    }

    private suspend fun successData(
        result: Pair<NetworkResult, TodoResponseList>,
        todoInteractor: TodoStorageInteractor,
        localList: List<TodoItem>,
        hideDoneItems: Boolean
    ): Pair<NetworkResult, List<TodoItem>> {
        val deletedList = todoInteractor.getDeletedItems()
        val unsyncedItems = todoInteractor.getUnsyncedItems()
        val updatedItems = localList.filter { changedItem ->
            unsyncedItems.find { it.id == changedItem.id } != null
        }
        val updatedInternetList = result.second.list.map { internetItem ->
            updatedItems.find { it.id == internetItem.id } ?: internetItem
        }.filter { updatedItem ->
            deletedList.find { it.id == updatedItem.id } == null
        }
        todoInteractor.clearAll()
        updatedInternetList.forEach { todoInteractor.addTodoItem(it) }
        updatedItems.forEach { todoInteractor.addTodoItem(it) }
        val syncResult = updatedInternetList + unsyncedItems
        val filteredList = if (hideDoneItems) {
            syncResult.filter { !it.done }
        } else {
            syncResult
        }
        return Pair(NetworkResult.SUCCESS_200, filteredList)
    }
}