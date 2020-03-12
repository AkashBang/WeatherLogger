package com.example.weatherlogger

import com.example.weatherlogger.data.network.model.*

internal const val WEATHER_DETAILS_CITY_NAME = "Yerandwane"
internal const val WEATHER_DETAILS_DESCRIPTION = "Cloudy skys"
internal const val WEATHER_DETAILS_LAT = "18.5"
internal const val WEATHER_DETAILS_LONG = "73.84"
internal const val WEATHER_DETAILS_API_RESPONSE =
    "{\"coord\":{\"lon\":73.84,\"lat\":18.5},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":26.89,\"feels_like\":25.51,\"temp_min\":26.89,\"temp_max\":26.89,\"pressure\":1015,\"humidity\":35,\"sea_level\":1015,\"grnd_level\":942},\"wind\":{\"speed\":2.09,\"deg\":266},\"clouds\":{\"all\":35},\"dt\":1583991475,\"sys\":{\"country\":\"IN\",\"sunrise\":1583975715,\"sunset\":1584018818},\"timezone\":19800,\"id\":1252736,\"name\":\"Yerandwane\",\"cod\":200}"


fun getWeatherApiResponse(): WeatherApiResponse {
    val coordinates = Coordinates(
        WEATHER_DETAILS_LAT.toDouble(),
        WEATHER_DETAILS_LONG.toDouble()
    )
    val weatherDetails = WeatherDetails(1, "Cloudy", WEATHER_DETAILS_DESCRIPTION, "1n2dp")
    val main = Main(18.99, 13.33, 10.00, 33.00, 193, 23)

    return WeatherApiResponse(
        coordinates, listOf(weatherDetails), "", main, 0, Wind(0.00, 0),
        Clouds(0), 19394394, Sys(1, 2, 23.00, "IN", 0, 0),
        1, 4, WEATHER_DETAILS_CITY_NAME, 2
    )
}