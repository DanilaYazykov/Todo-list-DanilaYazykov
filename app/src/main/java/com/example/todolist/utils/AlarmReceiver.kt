package com.example.todolist.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra("text")
        val importance = intent.getStringExtra("importance")
        if (text != null && importance != null) {
                notification(context, text, importance)
        }
    }

    private fun notification(context: Context, text : String, importance: String) {
        val notificationManager = getSystemService(context, NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, "TodoList")
            .setContentTitle("You have a task with importance: ${importance.lowercase()}")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        notificationManager?.notify(1, notification)
    }
}