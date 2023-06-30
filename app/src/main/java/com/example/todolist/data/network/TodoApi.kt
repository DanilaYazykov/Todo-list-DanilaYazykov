package com.example.todolist.data.network

import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.data.dto.TodoResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface TodoApi {

    @GET("list")
    suspend fun getList(): Response<TodoResponseList>

    @PATCH("list")
    suspend fun placeList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body list: TodoPostList
    ): Response<TodoPostList>
}