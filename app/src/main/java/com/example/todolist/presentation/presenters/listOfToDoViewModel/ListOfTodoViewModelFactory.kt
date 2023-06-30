package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.network.NetworkClientImpl
import com.example.todolist.data.network.TodoItemsRepositoryImpl
import com.example.todolist.data.sharedPreferences.TodoStorageManagerImpl
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.impl.TodoNetworkInteractorImpl
import com.example.todolist.domain.impl.TodoStorageInteractorImpl
import com.example.todolist.presentation.ui.util.CheckingInternet

class ListOfTodoViewModelFactory : ViewModelProvider.Factory {

    /**
     * внедрение di в следующем спринте, временно не трогаю
     */

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY])
        val todoInteractor = TodoStorageInteractorImpl(
            todoItemsRepository = TodoStorageManagerImpl(application)
        )

        val todoNetworkInteractor = TodoNetworkInteractorImpl(
            todoItemsRepository = TodoItemsRepositoryImpl(
                networkClient = NetworkClientImpl()
            )
        )
        val internet = CheckingInternet(application)

        @Suppress("UNCHECKED_CAST")
        return ListOfTodoViewModel(
            todoInteractor = todoInteractor,
            internet = internet,
            todoNetworkInteractor = todoNetworkInteractor
        ) as T
    }
}