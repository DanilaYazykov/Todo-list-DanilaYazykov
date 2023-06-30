package com.example.todolist.presentation.ui.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todolist.presentation.ui.list_of_to_do.ListOfToDoFragment

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val listOfToDoFragment = ListOfToDoFragment()

    override fun doWork(): Result {
        listOfToDoFragment.viewModel.syncTodoListFromNetwork()
        listOfToDoFragment.viewModel.updateDataServer()
        return Result.success()
    }
}