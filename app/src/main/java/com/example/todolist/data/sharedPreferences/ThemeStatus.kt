package com.example.todolist.data.sharedPreferences

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

class ThemeStatus @Inject constructor
    (private var sharedPrefs: SharedPreferences) : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences("theme", MODE_PRIVATE)
        switchTheme(sharedPrefs.getInt("ThemeStatus", 3))
    }
    init {
        themeStatus = sharedPrefs.getInt("ThemeStatus", 3)
    }

    fun switchTheme(theme: Int) {
        themeStatus = theme
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                1 -> AppCompatDelegate.MODE_NIGHT_YES
                2 -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
        sharedPrefs.edit().putInt("ThemeStatus", themeStatus).apply()
    }

    companion object {
        var themeStatus = 3
    }
}