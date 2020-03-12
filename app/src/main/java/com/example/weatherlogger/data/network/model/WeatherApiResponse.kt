package com.example.weatherlogger.data.network.model

import com.example.weatherlogger.domain.model.Weather
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class WeatherApiResponse(
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("weather") val weatherDetails: List<WeatherDetails>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
) {
    fun toWeather() = Weather(
        id,
        name,
        sys.country,
        weatherDetails[0].icon,
        weatherDetails[0].description,
        main.temp,
        main.feels_like,
        main.pressure,
        main.humidity,
        visibility,
        getDateFromMileSeconds(System.currentTimeMillis())
    )

    private fun getDateFromMileSeconds(millisecond: Long): String {
        val date = Date(millisecond)
        return SimpleDateFormat(
            "EEE, MMM d, ''yy  hh:mm a",
            Locale.getDefault()
        ).format(date)
    }
}

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feels_like: Double,
    @SerializedName("temp_min") val temp_min: Double,
    @SerializedName("temp_max") val temp_max: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)

data class Sys(
    @SerializedName("type") val type: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("message") val message: Double,
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Int,
    @SerializedName("sunset") val sunset: Int
)

data class WeatherDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Clouds(
    @SerializedName("all") val all: Int
)

data class Coordinates(
    @SerializedName("lon") val longitude: Double,
    @SerializedName("lat") val latitude: Double
)