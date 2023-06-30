package com.example.todolist.data.network

import android.util.Log
import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.data.dto.TodoResponseList
import com.example.todolist.domain.api.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoItemsRepositoryImpl(private val networkClient: NetworkClient) : TodoItemsRepository {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>> = flow {
        emit(networkClient.getListFromServer() as Pair<NetworkResult, TodoResponseList>)
    }

    override suspend fun placeListToServer(list: TodoPostList) {
        networkClient.placeListToServer(list)
    }
}