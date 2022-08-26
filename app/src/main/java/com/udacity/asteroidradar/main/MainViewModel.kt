package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.local.getDatabase
import com.udacity.asteroidradar.repo.AsteroidsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var today: String
    private lateinit var lastDay: String
    private val database = getDatabase(application)
    val apiKey = application.applicationContext.resources.getString(R.string.api_key)
    private val asteroidsRepository = AsteroidsRepository(database)


    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        refreshData(FilterOptions.ALL)
    }

    fun refreshData(filterOptions: FilterOptions) {

        setDates(filterOptions)
        viewModelScope.launch {

            try {
                if (filterOptions == FilterOptions.ALL)
                    asteroidsRepository.refreshAsteroids(apiKey)
                else
                    asteroidsRepository.refreshAsteroids(
                        today, lastDay, apiKey
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                asteroidsRepository.refreshPOD(apiKey)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    val asteroids = asteroidsRepository.asteroids
    val pod = asteroidsRepository.pod


    fun setDates(filterOptions: FilterOptions) {

        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        if (filterOptions == FilterOptions.WEEKLY) {
            today = dateFormat.format(currentTime)
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val lastTime = calendar.time
            lastDay = dateFormat.format(lastTime)
        } else if (filterOptions == FilterOptions.DAILY) {
            today = dateFormat.format(currentTime)
            lastDay = today
        }

    }
}

enum class FilterOptions {
    DAILY, WEEKLY, ALL
}