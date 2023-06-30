package com.example.todolist.domain.api

import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.data.dto.TodoResponseList
import com.example.todolist.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>>

    suspend fun placeListToServer(list: TodoPostList)

}