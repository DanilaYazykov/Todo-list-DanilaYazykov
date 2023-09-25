package com.example.todolist.presentation.viewModels.settingsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.sharedPreferences.ThemeStatus

/**
 * SettingsViewModelFactory - класс(фабрика), который отвечает за создание ViewModel для ListOfToDoFragment.
 */
class SettingsViewModelFactory @javax.inject.Inject constructor(
    private val themeStatus: ThemeStatus
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(
            themeStatus = themeStatus
        ) as T
    }
}