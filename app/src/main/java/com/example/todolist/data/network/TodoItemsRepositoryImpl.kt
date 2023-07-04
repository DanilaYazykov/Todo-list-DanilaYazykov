package com.example.todolist.data.network

import com.example.todolist.data.network.network.NetworkClient
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import com.example.todolist.domain.api.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoItemsRepositoryImpl(private val networkClient: NetworkClient) : TodoItemsRepository {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>> = flow {
        emit(networkClient.getListFromServer() as Pair<NetworkResult, TodoResponseList>)
    }

    override suspend fun placeListToServer(list: TodoPostList, revision: Int) {
        networkClient.placeListToServer(list, revision)
    }

    override suspend fun deleteItemFromServer(id: String, revision: Int) {
        networkClient.deleteItemFromServer(id, revision)
    }
}