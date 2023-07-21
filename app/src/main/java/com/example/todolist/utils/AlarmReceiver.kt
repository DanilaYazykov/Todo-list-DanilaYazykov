package com.example.todolist.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.todolist.R
import com.example.todolist.app.App

/**
 * AlarmReceiver - класс, который отвечает за получение данных из AlarmManager и создание уведомления.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra(TITLE)
        val importance = intent.getStringExtra(IMPORTANCE)
        if (text != null && importance != null) {
                notification(context, text, importance)
        }
    }

    private fun notification(context: Context, text : String, importance: String) {
        val notificationManager = getSystemService(context, NotificationManager::class.java)
        val importanceNew = customImportance(context, importance)
        val notification = NotificationCompat.Builder(context, App.CHANNEL_ID)
            .setContentTitle(context.getString(R.string.title_text) + " " + importanceNew.lowercase())
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        notificationManager?.notify(1, notification)
    }

    private fun customImportance(context: Context, importance: String): String {
        return when (importance) {
            "BASIC" -> context.getString(R.string.importance_basic)
            "IMPORTANT" -> context.getString(R.string.importance_high)
            "LOW" -> context.getString(R.string.importance_low)
            else -> context.getString(R.string.importance_basic)
        }
    }

    companion object {
        const val TITLE = "text"
        const val IMPORTANCE = "importance"
    }
}