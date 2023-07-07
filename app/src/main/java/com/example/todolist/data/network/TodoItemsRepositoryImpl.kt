package com.example.todolist.data.network

import com.example.todolist.data.network.network.NetworkClient
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import com.example.todolist.domain.api.TodoNetworkInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Класс для взаимодействия с сервером.
 * С сервером общается через интерфейс NetworkClient.
 */
class TodoItemsRepositoryImpl @Inject constructor
    (private val networkClient: NetworkClient) : TodoNetworkInteractor {

    override suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>> = flow {
        emit(networkClient.getListFromServer())
    }

    override suspend fun placeListToServer(list: TodoPostList, revision: Int) {
        networkClient.placeListToServer(list, revision)
    }

    override suspend fun deleteItemFromServer(id: String, revision: Int) {
        networkClient.deleteItemFromServer(id, revision)
    }
}