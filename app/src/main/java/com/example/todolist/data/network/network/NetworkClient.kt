package com.example.todolist.data.network.network

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList

/**
 * Интерфейс, который описывает запросы к серверу. Нужен для чистоты кода и связи подслоёв.
 */
interface NetworkClient {

    suspend fun getListFromServer() : Pair<NetworkResult, TodoResponseList>

    suspend fun placeListToServer(list: TodoPostList, revision: Int)

    suspend fun deleteItemFromServer(id: String, revision: Int)

}