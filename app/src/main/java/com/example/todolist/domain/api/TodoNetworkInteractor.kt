package com.example.todolist.domain.api

import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.models.TodoResponseList
import com.example.todolist.data.network.network.NetworkResult
import dagger.Provides
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс, который связывает слои Presentation и Domain.
 * Ввиду того, что имеет одинаковые методы, то также является интерфейсом для слоя Data.
 */
interface TodoNetworkInteractor {

    suspend fun getListFromServer(): Flow<Pair<NetworkResult, TodoResponseList>>

    suspend fun placeListToServer(list: TodoPostList, revision: Int)

    suspend fun deleteItemFromServer(id: String, revision: Int)
}