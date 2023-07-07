package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.domain.impl.TodoNetworkInteractorImpl
import com.example.todolist.domain.impl.TodoStorageInteractorImpl
import com.example.todolist.utils.CheckingInternet

/**
 * ListOfTodoViewModelFactory - класс(фабрика), который отвечает за создание ViewModel для ListOfToDoFragment.
 */
class ListOfTodoViewModelFactory @javax.inject.Inject constructor(
    private val todoInteractor: TodoStorageInteractorImpl,
    private val todoNetworkInteractor: TodoNetworkInteractorImpl,
    private val internet: CheckingInternet,
    private val dataParser: DataParser
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return ListOfTodoViewModel(
            todoInteractor = todoInteractor,
            internet = internet,
            todoNetworkInteractor = todoNetworkInteractor,
            dataParser = dataParser
        ) as T
    }
}