package com.example.todolist.presentation.ui.util

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModel

class SyncWorkerManager(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                application = applicationContext as Application
            ).create(ListOfTodoViewModel::class.java)
            viewModel.resetSyncFlag()
            viewModel.syncTodoListFromNetwork()

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}