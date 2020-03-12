package com.example.weatherlogger.data.network

import com.example.weatherlogger.data.network.model.WeatherApiResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val GET_WEATHER_ENDPOINT = "/data/2.5/weather"
        const val APP_ID = "8450e5f2b944b110afba218dab54c5a7"
        const val DEFAULT_UNITS = "metric"
    }
    @GET(GET_WEATHER_ENDPOINT)
    fun getWeatherByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") appId: String = APP_ID,
        @Query("units") units: String = DEFAULT_UNITS
    ): Single<Response<WeatherApiResponse>>

    @GET(GET_WEATHER_ENDPOINT)
    fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") appId: String = APP_ID,
        @Query("units") units: String = DEFAULT_UNITS
    ): Single<Response<WeatherApiResponse>>
}