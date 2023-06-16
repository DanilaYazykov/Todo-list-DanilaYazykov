package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.todoInteractor.TodoInteractorImpl

class ListOfTodoViewModelFactory : ViewModelProvider.Factory {

    /**
     * на данный момент нет информации, что будем использовать для di (koin или dagger),
     * поэтому использую Factory
     */

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY])
        val todoInteractor = TodoInteractorImpl(
            todoItemsRepository = TodoItemsRepositoryImpl(application)
        )

        @Suppress("UNCHECKED_CAST")
        return ListOfTodoViewModel(
            todoInteractor = todoInteractor
        ) as T
    }
}