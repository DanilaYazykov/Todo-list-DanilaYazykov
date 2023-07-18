package com.example.todolist.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
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

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "TodoList"
        const val CHANNEL_NAME = "TodoListChannel"
    }

}