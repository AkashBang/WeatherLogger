package com.example.weatherlogger.data.repository

import com.example.weatherlogger.data.local.WeatherDao
import com.example.weatherlogger.data.network.WeatherApiService
import com.example.weatherlogger.data.network.getBody
import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.domain.model.Weather
import io.reactivex.Completable
import io.reactivex.Single

class WeatherRepository(
    private val weatherApiService: WeatherApiService,
    private val weatherDao: WeatherDao
) {

    fun fetchWeatherByCoordinates(coordinates: Coordinates): Single<Weather> {
        val lat = coordinates.latitude.toString()
        val long = coordinates.longitude.toString()
        return weatherApiService
            .getWeatherByCoordinates(lat, long)
            .map { it.getBody()?.toWeather() }
    }

    fun fetchWeatherByCityName(cityName: String): Single<Weather> {
        return weatherApiService
            .getWeatherByCityName(cityName)
            .map { it.getBody()?.toWeather() }
    }

    fun geLocallyStoredWeatherDetails(): Single<List<Weather>> {
        return weatherDao.getAll()
    }

    fun storeWeatherDetailsLocally(weather: Weather): Completable {
        return weatherDao.insert(weather)
    }

}