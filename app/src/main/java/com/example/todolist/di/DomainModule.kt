package com.example.todolist.di

import com.example.todolist.data.dataBase.impl.TodoLocalStorageImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

   @Provides
    fun provideTodoLocalStorage(todoLocalStorage: TodoLocalStorageImpl
   ): TodoLocalStorageImpl {
       return todoLocalStorage
   }

}