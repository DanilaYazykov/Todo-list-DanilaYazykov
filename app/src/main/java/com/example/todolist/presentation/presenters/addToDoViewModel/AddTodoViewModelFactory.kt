package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.todoInteractor.TodoInteractorImpl

class AddTodoViewModelFactory : ViewModelProvider.Factory {
    /**
     * на данный момент нет информации, что будем использовать для di (koin или dagger),
     * поэтому использую Factory
     */

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
        val todoInteractor = TodoInteractorImpl(
            todoItemsRepository = TodoItemsRepositoryImpl(application)
        )

        @Suppress("UNCHECKED_CAST")
        return AddTodoViewModel(
            todoInteractor = todoInteractor
        ) as T
    }
}