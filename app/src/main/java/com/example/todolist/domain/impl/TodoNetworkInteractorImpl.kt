package com.example.todolist.domain.impl

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import com.example.todolist.data.network.network.NetworkResult

import com.example.todolist.domain.api.TodoNetworkInteractor
import kotlinx.coroutines.flow.Flow

/**
 * Domain класс. Содержит в себе бизнес-логику приложения.
 */
class TodoNetworkInteractorImpl(private val todoItemsRepository: TodoNetworkInteractor) : TodoNetworkInteractor {

    override suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>> {
        return todoItemsRepository.getListFromServer()
    }

    override suspend fun placeListToServer(list: TodoPostList, revision: Int) {
        todoItemsRepository.placeListToServer(list, revision)
    }

    override suspend fun deleteItemFromServer(id: String, revision: Int) {
        todoItemsRepository.deleteItemFromServer(id, revision)
    }

}