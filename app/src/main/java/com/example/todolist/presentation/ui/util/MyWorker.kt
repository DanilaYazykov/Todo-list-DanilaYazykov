package com.example.todolist.presentation.ui.util

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModel

class MyWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                applicationContext as Application
            ).create(ListOfTodoViewModel::class.java)
            viewModel.syncTodoListFromNetwork()
            viewModel.updateDataServer()

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}