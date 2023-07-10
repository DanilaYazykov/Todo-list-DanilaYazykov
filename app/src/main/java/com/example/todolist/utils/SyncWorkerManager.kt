package com.example.todolist.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.app.App
import com.example.todolist.data.network.network.NetworkClient
import javax.inject.Inject

/**
 * SyncWorkerManager - класс, который отвечает за синхронизацию данных с сервером в оффлайне.
 */
class SyncWorkerManager (
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

     @Inject
     lateinit var networkClient: NetworkClient

    init {
        (appContext.applicationContext as App).appComponent.syncWorkerComponentFactory().create().inject(this)
    }

    override suspend fun doWork(): Result {
        return try {
            networkClient.getListFromServer()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}