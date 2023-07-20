package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.dataBase.converters.DeleteItemDbConverter
import com.example.todolist.domain.impl.TodoLocalInteractorImpl

/**
 * AddTodoViewModelFactory - класс(фабрика), который отвечает за создание ViewModel для AddTodoFragment.
 */
class AddTodoViewModelFactory @javax.inject.Inject constructor(
    private val database: TodoLocalInteractorImpl,
    private val converter: DeleteItemDbConverter
     ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return AddTodoViewModel(
            database = database,
            converter = converter
        ) as T
    }
}