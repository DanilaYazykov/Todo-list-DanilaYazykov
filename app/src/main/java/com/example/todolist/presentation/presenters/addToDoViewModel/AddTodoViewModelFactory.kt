package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.dataBase.AppDatabase

/**
 * AddTodoViewModelFactory - класс(фабрика), который отвечает за создание ViewModel для AddTodoFragment.
 */
class AddTodoViewModelFactory @javax.inject.Inject constructor(
     private val database: AppDatabase
     ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return AddTodoViewModel(
            database = database
        ) as T
    }
}