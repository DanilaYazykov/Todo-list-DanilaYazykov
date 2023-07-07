package com.example.todolist.di

import com.example.todolist.data.sharedPreferences.TodoLocalStorageImpl
import com.example.todolist.domain.impl.TodoStorageInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

   @Provides
    fun provideTodoInteractor(todoLocalStorage: TodoLocalStorageImpl
   ): TodoStorageInteractorImpl {
        return TodoStorageInteractorImpl(todoItemsRepository = todoLocalStorage)
    }

}