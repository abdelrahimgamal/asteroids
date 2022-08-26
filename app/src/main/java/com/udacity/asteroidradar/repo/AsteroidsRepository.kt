package com.udacity.asteroidradar.repo

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.local.AsteroidDatabase
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {


    /**
     * A list of asteroids that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()
    val pod: LiveData<PictureOfDay> = database.podDao.getPOD()


    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshAsteroids(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        Network.asteroids.getAsteroids(
                            startDate,
                            endDate,
                            apiKey
                        )
                    )
                )
            database.asteroidDao.clear()
            database.asteroidDao.insertAll(asteroids)
        }
    }
    suspend fun refreshAsteroids( apiKey: String) {
        withContext(Dispatchers.IO) {
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        Network.asteroids.getAsteroids(
                            apiKey
                        )
                    )
                )
            database.asteroidDao.clear()
            database.asteroidDao.insertAll(asteroids)
        }
    }

    /**
     * GET NASA Picture of the day.
     * This function uses the IO dispatcher to ensure the server calling operation
     */
    suspend fun refreshPOD(apiKey: String) {
        withContext(Dispatchers.IO) {
            val pictureOfDay = Network.asteroids.getPictureOfDay(
                apiKey
            )
            database.podDao.delete()
            database.podDao.insert(pictureOfDay)
        }
    }
}