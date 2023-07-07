package com.example.todolist.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.app.App
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModel
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModelFactory
import javax.inject.Inject

/**
 * SyncWorkerManager - класс, который отвечает за синхронизацию данных с сервером в офлайне.
 */
class SyncWorkerManager(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var viewModelFactory: ListOfTodoViewModelFactory

    init {
        (appContext.applicationContext as App).appComponent.syncWorkerComponentFactory().create().inject(this)
    }

    override suspend fun doWork(): Result {
        return try {
            val viewModelStore = ViewModelStore()
            val viewModelProvider = ViewModelProvider(viewModelStore, viewModelFactory)
            val viewModel = viewModelProvider[ListOfTodoViewModel::class.java]
            viewModel.resetSyncFlag()
            viewModel.syncTodoListFromNetwork()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}