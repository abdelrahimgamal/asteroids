package com.udacity.asteroidradar.network

import androidx.databinding.ktx.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * A retrofit service to fetch NASA data.
 */
interface NeoWSService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") apiKey: String,
    ): String

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String,
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String,
    ): PictureOfDay
}


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }

    val asteroids = retrofit.create(NeoWSService::class.java)
}
