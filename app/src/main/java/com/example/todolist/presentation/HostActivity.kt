package com.example.todolist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todolist.databinding.ActivityHostBinding
import com.example.todolist.presentation.ui.util.MyWorker
import java.util.concurrent.TimeUnit

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repeatInterval = 8L
        val repeatIntervalTimeUnit = TimeUnit.HOURS
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(repeatInterval, repeatIntervalTimeUnit)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}