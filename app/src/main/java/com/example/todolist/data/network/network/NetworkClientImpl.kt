package com.example.todolist.data.network.network

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Класс для взаимодействия с сервером. Непосредственно отправляет запросы.
 */
class NetworkClientImpl : NetworkClient {

    private var loggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        chain.proceed(request)
    }

    private val apiService = createApiService()

    private fun createApiService(): TodoApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TodoApi::class.java)
    }

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
        return when {
            response.code() == CODE_200 && response.body() != null && response.body()!!.list.isNotEmpty() -> {
                val todoResponseList = response.body()!!
                Pair(NetworkResult.SUCCESS_200, todoResponseList)
            }
            response.code() == CODE_200 && response.body() != null && response.body()!!.list.isEmpty() -> {
                Pair(NetworkResult.SUCCESS_200, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
            response.code() == CODE_400 -> {
                Pair(NetworkResult.ERROR_UNSYNCHRONIZED_DATA_400, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
            response.code() == CODE_401 -> {
                Pair(NetworkResult.UNCORRECT_AUTHORIZATION_401, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
            response.code() == CODE_404 -> {
                Pair(NetworkResult.ID_TODO_NOT_FOUND_404, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
            response.code() == CODE_500 -> {
                Pair(NetworkResult.ERROR_SERVER_500, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
            else -> {
                Pair(NetworkResult.UNKNOWN_ERROR, TodoResponseList(list = emptyList(), revision = response.body()!!.revision))
            }
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

    companion object{
        const val CODE_200 = 200
        const val CODE_400 = 400
        const val CODE_401 = 401
        const val CODE_404 = 404
        const val CODE_500 = 500
    }
}