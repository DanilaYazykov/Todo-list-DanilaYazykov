package com.example.todolist.domain.impl

import android.util.Log
import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.data.dto.TodoResponseList
import com.example.todolist.data.network.NetworkResult
import com.example.todolist.domain.api.TodoItemsRepository
import com.example.todolist.domain.api.TodoNetworkInteractor
import kotlinx.coroutines.flow.Flow

class TodoNetworkInteractorImpl(private val todoItemsRepository: TodoItemsRepository) : TodoNetworkInteractor {

    override suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>> {
        return todoItemsRepository.getListFromServer()
    }

    override suspend fun placeListToServer(list: TodoPostList) {
        todoItemsRepository.placeListToServer(list)
    }

}