package com.example.todolist.di

import com.example.todolist.app.App
import com.example.todolist.presentation.HostActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DomainModule::class,
        DataModule::class,
        PresentationModule::class,
        BindModule::class
    ]
)
interface AppComponent {

    fun fragmentComponentFactory(): FragmentComponent.Factory

    fun syncWorkerComponentFactory(): SyncWorkerComponent.Factory

    fun inject(app: App)

    fun inject(hostActivity: HostActivity)
}