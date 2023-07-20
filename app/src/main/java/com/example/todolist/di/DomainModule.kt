package com.example.todolist.di

import com.example.todolist.domain.dataBase.TodoLocalInteractor
import com.example.todolist.domain.impl.TodoLocalInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

        @Provides
        fun provideTodoLocalInteractor(todoLocalInteractor: TodoLocalInteractorImpl): TodoLocalInteractor {
            return todoLocalInteractor
        }

}