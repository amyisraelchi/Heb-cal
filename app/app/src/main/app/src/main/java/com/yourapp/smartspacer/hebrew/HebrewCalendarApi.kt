package com.yourapp.smartspacer.hebrew

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for Hebrew Calendar endpoints
 */
interface HebrewCalendarApi {

    @GET("api/hebdate")
    suspend fun getHebdate(
        @Query("date") date: String? = null
    ): Response<HebdateResponse>

    @GET("api/zman")
    suspend fun getZman(
        @Query("date") date: String? = null,
        @Query("lat") latitude: Double? = null,
        @Query("lng") longitude: Double? = null
    ): Response<ZmanResponse>

    @GET("api/calendar")
    suspend fun getCalendar(
        @Query("date") date: String? = null,
        @Query("lat") latitude: Double? = null,
        @Query("lng") longitude: Double? = null
    ): Response<CalendarResponse>

    companion object {
        // Replace with your published Replit URL
        private const val BASE_URL = "https://hebcal-zmanim.replit.app/"

        fun create(): HebrewCalendarApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HebrewCalendarApi::class.java)
        }
    }
}
