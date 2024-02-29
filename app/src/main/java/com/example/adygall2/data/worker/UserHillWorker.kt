package com.example.adygall2.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.adygall2.data.room.userbase.dao.UserDao
import com.example.adygall2.data.room.userbase.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserHillWorker(appContext: Context, params: WorkerParameters, private val dao: UserDao) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result  = withContext(Dispatchers.IO) {
            return@withContext try {
                var user = dao.getUser()
                // hill
                user = user.copy(hp = user.hp + 10)
                dao.upsertUser(user)
                Result.success(outputData(user))
            } catch (ex: Exception) {
                Result.failure()
            }
        }

    private fun outputData(user: UserEntity) =
        Data.Builder()
            .putInt("hp", user.hp)
            .build()
}