package com.example.todolist.presentation.viewModels.settingsViewModel

import androidx.lifecycle.ViewModel
import com.example.todolist.data.sharedPreferences.ThemeStatus

/** SettingsViewModel - viewModel UI класса SettingsFragment, который отвечает за логику в SettingsFragment */
class SettingsViewModel(
    private val themeStatus: ThemeStatus
): ViewModel() {

    fun action(event: SettingsEvent) {
        when (event) {
            is SwitchEvent -> switchTheme(event.checked)
        }
    }

    private fun switchTheme(checked: Int) {
        themeStatus.switchTheme(checked)
    }

}