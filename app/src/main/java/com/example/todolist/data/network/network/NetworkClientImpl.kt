package com.example.todolist.data.network.network

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
    private val apiService: TodoApi
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

    private fun showResult(response: Response<TodoResponseList>): Pair<NetworkResult, TodoResponseList> {
        val body = response.body()
        val revision = body?.revision ?: 0

        return when (response.code()) {
            CODE_200 -> {
                if (body != null && body.list.isNotEmpty()) {
                    Pair(NetworkResult.SUCCESS_200, body)
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
        val result = apiService.placeList(revision = revision, list = list)
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