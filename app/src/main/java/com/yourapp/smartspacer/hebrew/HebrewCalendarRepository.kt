package com.yourapp.smartspacer.hebrew

import android.content.Context
import android.location.Location
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository for fetching Hebrew calendar data with caching
 */
class HebrewCalendarRepository(private val context: Context) {

    private val api = HebrewCalendarApi.create()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Simple in-memory cache
    private var cachedHebdate: Pair<String, HebdateResponse>? = null
    private var cachedZman: Triple<String, Double?, ZmanResponse>? = null

    /**
     * Get Hebrew date for today or specific date
     */
    suspend fun getHebdate(date: Date = Date()): Result<HebdateResponse> = withContext(Dispatchers.IO) {
        try {
            val dateString = dateFormat.format(date)

            // Check cache
            cachedHebdate?.let { (cachedDate, response) ->
                if (cachedDate == dateString) return@withContext Result.success(response)
            }

            // Fetch from API
            val response = api.getHebdate(dateString)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                cachedHebdate = dateString to body
                Result.success(body)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching hebdate", e)
            Result.failure(e)
        }
    }

    /**
     * Get zmanim (prayer times) for location
     */
    suspend fun getZman(
        location: Location? = null,
        date: Date = Date()
    ): Result<ZmanResponse> = withContext(Dispatchers.IO) {
        try {
            val dateString = dateFormat.format(date)
            val lat = location?.latitude
            val lng = location?.longitude

            // Check cache
            cachedZman?.let { (cachedDate, cachedLat, response) ->
                if (cachedDate == dateString && cachedLat == lat) {
                    return@withContext Result.success(response)
                }
            }

            // Fetch from API
            val response = api.getZman(dateString, lat, lng)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                cachedZman = Triple(dateString, lat, body)
                Result.success(body)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching zman", e)
            Result.failure(e)
        }
    }

    /**
     * Get calendar info (parsha, holidays, candle lighting)
     */
    suspend fun getCalendar(
        location: Location? = null,
        date: Date = Date()
    ): Result<CalendarResponse> = withContext(Dispatchers.IO) {
        try {
            val dateString = dateFormat.format(date)
            val lat = location?.latitude
            val lng = location?.longitude

            val response = api.getCalendar(dateString, lat, lng)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching calendar", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "HebrewCalendarRepo"
    }
}
