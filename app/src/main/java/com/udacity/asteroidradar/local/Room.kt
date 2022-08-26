package com.udacity.asteroidradar.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface AsteroidDao {
    @Query("select * from asteroid ")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: ArrayList<Asteroid>)

    @Query("DELETE FROM asteroid")
    fun clear()
}

@Dao
interface PictureOfDayDao {
    @Query("select * from pictureofday")
    fun getPOD(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureOfDay: PictureOfDay)

    @Query("DELETE FROM pictureofday")
    fun delete()
}


@Database(entities = [Asteroid::class, PictureOfDay::class], version = 2)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val podDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroidsDB"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}