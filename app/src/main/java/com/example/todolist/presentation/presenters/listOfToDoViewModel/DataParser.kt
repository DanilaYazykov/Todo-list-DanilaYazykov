package com.example.todolist.presentation.presenters.listOfToDoViewModel

import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.data.dataBase.domain.impl.DeletedItemDaoImpl
import com.example.todolist.data.dataBase.domain.impl.TodoLocalDaoImpl
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.domain.models.TodoResponseList

/**
 * DataParser - класс, который отвечает за синхронизацию полученных данных и выдачу результата.
 */
class DataParser {

    suspend fun parseData(
        result: Pair<NetworkResult, TodoResponseList>,
        localList: List<TodoItem>,
        hideDoneItems: Boolean,
        database: TodoLocalDaoImpl,
        databaseOffline: DeletedItemDaoImpl,
    ): Pair<NetworkResult, List<TodoItem>> {
        return syncronizingLocalAndNetwork(result, localList, hideDoneItems, database, databaseOffline)
    }

    private suspend fun syncronizingLocalAndNetwork(
        result: Pair<NetworkResult, TodoResponseList>,
        localList: List<TodoItem>,
        hideDoneItems: Boolean,
        database: TodoLocalDaoImpl,
        databaseOffline: DeletedItemDaoImpl,
    ): Pair<NetworkResult, List<TodoItem>> {
        return when (result.first) {
            NetworkResult.SUCCESS_200 -> {
                successData(result, localList, hideDoneItems, database, databaseOffline)
            }
            else -> Pair(result.first, emptyList())
        }
    }

    private suspend fun successData(
        result: Pair<NetworkResult, TodoResponseList>,
        localList: List<TodoItem>,
        hideDoneItems: Boolean,
        database: TodoLocalDaoImpl,
        databaseOffline: DeletedItemDaoImpl,
    ): Pair<NetworkResult, List<TodoItem>> {
        val deletedList = databaseOffline.getDeletedItems()
        val unsyncedItems = database.getUnsyncedItems()
        val updatedItems = localList.filter { changedItem ->
            unsyncedItems.find { it.id == changedItem.id } != null
        }
        val updatedInternetList = result.second.list.map { internetItem ->
            updatedItems.find { it.id == internetItem.id } ?: internetItem
        }.filter { updatedItem -> deletedList.find { it.id == updatedItem.id } == null }
        database.deleteAllTodoItems()
        updatedInternetList.forEach { database.insertTodoItem(it) }
        updatedItems.forEach { database.insertTodoItem(it) }
        val syncResult = updatedInternetList + unsyncedItems
        val filteredList = if (hideDoneItems) {
            syncResult.filter { !it.done }
        } else {
            syncResult
        }
        return Pair(NetworkResult.SUCCESS_200, filteredList)
    }
}