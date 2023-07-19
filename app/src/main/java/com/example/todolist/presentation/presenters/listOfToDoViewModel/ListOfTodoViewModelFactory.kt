package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.dataBase.domain.impl.DeletedItemDaoImpl
import com.example.todolist.data.dataBase.domain.impl.TodoLocalDaoImpl
import com.example.todolist.domain.impl.TodoNetworkInteractorImpl
import com.example.todolist.utils.NetworkStateReceiver

/**
 * ListOfTodoViewModelFactory - класс(фабрика), который отвечает за создание ViewModel для ListOfToDoFragment.
 */
class ListOfTodoViewModelFactory @javax.inject.Inject constructor(
    private val todoNetworkInteractor: TodoNetworkInteractorImpl,
    private val internetReceive: NetworkStateReceiver,
    private val database: TodoLocalDaoImpl,
    private val databaseOffline: DeletedItemDaoImpl,
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return ListOfTodoViewModel(
            internetReceive = internetReceive,
            todoNetworkInteractor = todoNetworkInteractor,
            database = database,
            databaseOffline = databaseOffline,
        ) as T
    }
}