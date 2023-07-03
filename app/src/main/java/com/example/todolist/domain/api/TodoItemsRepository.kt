package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import com.example.todolist.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>>

    suspend fun placeListToServer(list: TodoPostList, revision: Int)

    suspend fun deleteItemFromServer(id: String, revision: Int)

}