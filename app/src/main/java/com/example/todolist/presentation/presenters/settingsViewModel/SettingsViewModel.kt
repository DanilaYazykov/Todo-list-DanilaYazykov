package com.example.todolist.presentation.presenters.settingsViewModel

import androidx.lifecycle.ViewModel
import com.example.todolist.data.sharedPreferences.ThemeStatus

class SettingsViewModel(
    private val themeStatus: ThemeStatus
): ViewModel() {

    fun switchTheme(checked: Int) {
        themeStatus.switchTheme(checked)
    }

}