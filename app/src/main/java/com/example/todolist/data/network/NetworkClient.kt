package com.example.todolist.data.network

import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.data.dto.TodoResponseList

interface NetworkClient {

    suspend fun getListFromServer() : Any

    suspend fun placeListToServer(dto: TodoPostList)

}