package com.example.adygall2.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.adygall2.data.room.userbase.dao.UserDao

class UserWorkerFactory(private val dao: UserDao): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return if (workerClassName == UserHillWorker::class.java.name) {
            UserHillWorker(appContext, workerParameters, dao)
        } else null
    }
}