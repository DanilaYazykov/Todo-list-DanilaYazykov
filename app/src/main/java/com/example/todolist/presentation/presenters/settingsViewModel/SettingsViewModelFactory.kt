package com.example.todolist.presentation.presenters.settingsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.data.sharedPreferences.ThemeStatus

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