/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.local.getDatabase
import com.udacity.asteroidradar.repo.AsteroidsRepository
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class RefreshDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)

        val repository = AsteroidsRepository(database)


        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val lastTime = calendar.time
        val lastDay = dateFormat.format(lastTime)
        return try {
            repository.refreshAsteroids(
                today,
                lastDay,
                applicationContext.getString(R.string.app_name)
            )
            repository.refreshPOD(applicationContext.getString(R.string.app_name))
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}


