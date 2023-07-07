package com.example.todolist.di

import android.content.Context
import com.example.todolist.presentation.presenters.listOfToDoViewModel.DataParser
import com.example.todolist.presentation.ui.listOfToDo.RenderClass
import com.example.todolist.utils.CheckingInternet
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    fun provideInternet(context: Context): CheckingInternet {
        return CheckingInternet(context = context)
    }

    @Singleton
    @Provides
    fun provideDataParser(): DataParser {
        return DataParser()
    }

    @Singleton
    @Provides
    fun provideRender(): RenderClass {
        return RenderClass()
    }
}