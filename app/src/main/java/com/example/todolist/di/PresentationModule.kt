package com.example.todolist.di

import android.content.Context
import com.example.todolist.presentation.ui.listOfToDo.RenderClass
import com.example.todolist.utils.CheckingPermission
import com.example.todolist.utils.NetworkStateReceiver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    fun provideInternetReceiver(context: Context): NetworkStateReceiver {
        return NetworkStateReceiver(context = context)
    }

    @Singleton
    @Provides
    fun provideRender(): RenderClass {
        return RenderClass()
    }

    @Singleton
    @Provides
    fun provideCheckPermission(context: Context) : CheckingPermission {
        return CheckingPermission(context = context)
    }
}