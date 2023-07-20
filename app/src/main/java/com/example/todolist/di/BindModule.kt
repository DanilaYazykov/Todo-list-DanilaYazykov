package com.example.todolist.di

import com.example.todolist.data.dataBase.TodoLocalStorageImpl
import com.example.todolist.data.network.TodoItemsRepositoryImpl
import com.example.todolist.data.network.network.NetworkClient
import com.example.todolist.data.network.network.NetworkClientImpl
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.dataBase.TodoLocalStorage
import dagger.Binds
import dagger.Module

@Module
interface BindModule {

    @Binds
    fun provideTodoNetworkInteractor(
        todoItemsRepository: TodoItemsRepositoryImpl
    ): TodoNetworkInteractor

    @Binds
    fun provideNetworkClient(
        networkClient: NetworkClientImpl
    ): NetworkClient

    @Binds
    fun provideTodoLocalDao(
        todoLocalDaoImpl: TodoLocalStorageImpl
    ): TodoLocalStorage
}

/**
 * IDE не способен распознать динамические связи.
 * Без этого интерфейса приложение не скомпилируется.
 */