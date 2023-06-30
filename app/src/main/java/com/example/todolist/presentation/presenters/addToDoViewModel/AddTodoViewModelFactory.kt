package com.example.todolist.presentation.presenters.addToDoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.sharedPreferences.TodoStorageManagerImpl
import com.example.todolist.domain.impl.TodoStorageInteractorImpl

class AddTodoViewModelFactory : ViewModelProvider.Factory {
    /**
     * внедрение di в следующем спринте, временно не трогаю
     */

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
        val todoInteractor = TodoStorageInteractorImpl(
            todoItemsRepository = TodoStorageManagerImpl(application)
        )

        @Suppress("UNCHECKED_CAST")
        return AddTodoViewModel(
            todoInteractor = todoInteractor
        ) as T
    }
}