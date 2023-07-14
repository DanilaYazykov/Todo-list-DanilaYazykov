package com.example.todolist.app

import android.app.Application
import com.example.todolist.di.AppComponent
import com.example.todolist.di.AppModule
import com.example.todolist.di.DaggerAppComponent
import com.example.todolist.data.sharedPreferences.ThemeStatus
import javax.inject.Inject

class App: Application() {

    lateinit var appComponent: AppComponent
    @Inject
    lateinit var themeStatus: ThemeStatus

    override fun onCreate() {
        super.onCreate()

       appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
        appComponent.inject(this)
        themeStatus.switchTheme(ThemeStatus.themeStatus)
    }

}