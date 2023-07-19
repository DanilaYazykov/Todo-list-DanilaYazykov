package com.example.todolist.data.network.network

import com.example.todolist.data.dataBase.domain.impl.DeletedItemDaoImpl
import com.example.todolist.data.dataBase.domain.impl.TodoLocalDaoImpl
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Класс для взаимодействия с сервером. Непосредственно отправляет и принимает запросы.
 */
class NetworkClientImpl @Inject constructor(
    private val apiService: TodoApi,
    private val databaseOffline: DeletedItemDaoImpl,
    private val database: TodoLocalDaoImpl
) : NetworkClient {

    override suspend fun getListFromServer(): Pair<NetworkResult, TodoResponseList> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getList()
                showResult(response)
            } catch (e: Exception) {
                Pair(NetworkResult.UNKNOWN_ERROR, TodoResponseList(list = emptyList(), revision = 0))
            }
        }

    private suspend fun mainSync(localList: List<TodoItem>): List<TodoItem>  {
        val deletedList = databaseOffline.getDeletedItems()
        val unsyncedItems = database.getUnsyncedItems()
        val updatedItems = localList.filter { changedItem ->
            unsyncedItems.find { it.id == changedItem.id } != null
        }
        val updatedInternetList = localList.map { internetItem ->
            updatedItems.find { it.id == internetItem.id } ?: internetItem
        }.filter { item -> !deletedList.any { deletedItem -> deletedItem.id == item.id }  }
        database.deleteAllTodoItems()
        val allItems = updatedInternetList + unsyncedItems
        database.insertListTodoItem(allItems)
        return allItems
    }

    private suspend fun showResult(response: Response<TodoResponseList>): Pair<NetworkResult, TodoResponseList> {
        val body = response.body()
        val revision = body?.revision ?: 0

        return when (response.code()) {
            CODE_200 -> {
                if (body != null && body.list.isNotEmpty()) {
                    val filteredList = mainSync(body.list)
                    databaseOffline.deleteAllDeletedItems()
                    Pair(NetworkResult.SUCCESS_200, TodoResponseList(list = filteredList, revision = revision))
                } else {
                    Pair(NetworkResult.SUCCESS_200, TodoResponseList(list = emptyList(), revision = revision))
                }
            }
            CODE_400 -> Pair(NetworkResult.ERROR_UNSYNCHRONIZED_DATA_400, TodoResponseList(list = emptyList(), revision = revision))
            CODE_401 -> Pair(NetworkResult.UNCORRECT_AUTHORIZATION_401, TodoResponseList(list = emptyList(), revision = revision))
            CODE_404 -> Pair(NetworkResult.ID_TODO_NOT_FOUND_404, TodoResponseList(list = emptyList(), revision = revision))
            CODE_500 -> Pair(NetworkResult.ERROR_SERVER_500, TodoResponseList(list = emptyList(), revision = revision))
            else -> Pair(NetworkResult.UNKNOWN_ERROR, TodoResponseList(list = emptyList(), revision = revision))
        }
    }

    override suspend fun placeListToServer(list: TodoPostList, revision: Int) {
        withContext(Dispatchers.IO) {
        apiService.placeList(revision = revision, list = list)
        }
    }

    override suspend fun deleteItemFromServer(id: String, revision: Int) {
        withContext(Dispatchers.IO) {
            apiService.deleteItem(id = id, revision = revision)
        }
    }

    companion object {
        const val ID_TOKEN = "Bearer conveyable"
        const val CODE_200 = 200
        const val CODE_400 = 400
        const val CODE_401 = 401
        const val CODE_404 = 404
        const val CODE_500 = 500
    }
}