package com.example.todolist.data.network.network

import com.example.todolist.domain.models.TodoPostList

interface NetworkClient {

    suspend fun getListFromServer() : Any

    suspend fun placeListToServer(list: TodoPostList, revision: Int)

    suspend fun deleteItemFromServer(id: String, revision: Int)

}