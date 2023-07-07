package com.example.todolist.app

import android.app.Application
import com.example.todolist.di.AppComponent
import com.example.todolist.di.AppModule
import com.example.todolist.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

       appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
    }

}