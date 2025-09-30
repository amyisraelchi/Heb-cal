package com.yourapp.smartspacer.hebrew

import com.google.gson.annotations.SerializedName

/**
 * Data models for Hebrew Calendar API responses
 */

data class HebdateResponse(
    @SerializedName("hebdate") val hebdate: String,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String
)

data class ZmanResponse(
    @SerializedName("zman") val zman: String,
    @SerializedName("midday") val midday: String,
    @SerializedName("sunset") val sunset: String,
    @SerializedName("dawn") val dawn: String?,
    @SerializedName("sunrise") val sunrise: String?,
    @SerializedName("dusk") val dusk: String?,
    @SerializedName("nightfall") val nightfall: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String
)

data class CalendarResponse(
    @SerializedName("parsha") val parsha: String?,
    @SerializedName("holiday") val holiday: String?,
    @SerializedName("candleLighting") val candleLighting: String?,
    @SerializedName("havdalah") val havdalah: String?,
    @SerializedName("date") val date: String,
    @SerializedName("location") val location: String?,
    @SerializedName("status") val status: String
)

data class ErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)
