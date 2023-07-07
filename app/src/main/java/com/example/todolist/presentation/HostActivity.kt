package com.example.todolist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todolist.databinding.ActivityHostBinding
import com.example.todolist.utils.SyncWorkerManager
import java.util.concurrent.TimeUnit

/**
 * HostActivity - главная(root) Activity приложения.
 */
class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsOfWorkerManager()
    }

    private fun settingsOfWorkerManager() {
        val repeatInterval = TIME_OF_REPEAT
        val repeatIntervalTimeUnit = TimeUnit.HOURS
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<SyncWorkerManager>(repeatInterval, repeatIntervalTimeUnit)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

    companion object {
        const val TIME_OF_REPEAT = 8L
    }
}