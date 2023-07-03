package com.example.todolist.data.network

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseElement
import com.example.todolist.domain.models.TodoResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface TodoApi {

    @GET("list")
    suspend fun getList(): Response<TodoResponseList>

    @PATCH("list")
    suspend fun placeList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body list: TodoPostList
    ): Response<TodoPostList>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoResponseElement>

}