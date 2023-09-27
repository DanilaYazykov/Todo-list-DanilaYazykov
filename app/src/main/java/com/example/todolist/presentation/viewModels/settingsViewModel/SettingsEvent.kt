package com.example.todolist.presentation.viewModels.settingsViewModel

sealed interface SettingsEvent

class SwitchEvent(val checked: Int) : SettingsEvent