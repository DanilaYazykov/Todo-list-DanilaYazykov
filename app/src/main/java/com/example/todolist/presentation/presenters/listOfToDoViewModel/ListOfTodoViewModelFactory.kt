package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.sharedPreferences.TodoStorageManagerImpl
import com.example.todolist.domain.todoInteractor.TodoStorageInteractorImpl

class ListOfTodoViewModelFactory : ViewModelProvider.Factory {

    /**
     * внедрение di в следующем спринте, временно не трогаю
     */

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY])
        val todoInteractor = TodoStorageInteractorImpl(
            todoItemsRepository = TodoStorageManagerImpl(application)
        )

        @Suppress("UNCHECKED_CAST")
        return ListOfTodoViewModel(
            todoInteractor = todoInteractor
        ) as T
    }
}