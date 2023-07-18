package com.example.todolist.di

import com.example.todolist.presentation.ui.addToDo.AddToDoFragment
import com.example.todolist.presentation.ui.listOfToDo.ListOfToDoFragment
import com.example.todolist.presentation.ui.settings.SettingsFragment
import com.example.todolist.utils.SyncWorkerManager
import dagger.Subcomponent
import javax.inject.Scope

@FragmentScope
@Subcomponent
interface FragmentComponent {

    fun inject(fragment: AddToDoFragment)

    fun inject(fragment: ListOfToDoFragment)

    fun inject(fragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope


@SyncWorkerScope
@Subcomponent
interface SyncWorkerComponent {

    fun inject(syncWorkerManager: SyncWorkerManager)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SyncWorkerComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SyncWorkerScope