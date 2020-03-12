package com.example.weatherlogger.data.repository

import com.example.weatherlogger.WEATHER_DETAILS_CITY_NAME
import com.example.weatherlogger.WEATHER_DETAILS_LAT
import com.example.weatherlogger.WEATHER_DETAILS_LONG
import com.example.weatherlogger.data.local.WeatherDao
import com.example.weatherlogger.data.network.WeatherApiService
import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.getWeatherApiResponse
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class WeatherRepositoryTest {

    private val weatherDao = mock(WeatherDao::class.java)
    private val weatherApiService = mock(WeatherApiService::class.java)
    private val weatherRepository = WeatherRepository(
        weatherDao = weatherDao,
        weatherApiService = weatherApiService
    )

    @Test
    fun `should return weather by coordinates`() {
        val singleApiResponse = Single.fromCallable {
            return@fromCallable Response.success(getWeatherApiResponse())
        }
        `when`(
            weatherApiService.getWeatherByCoordinates(
                WEATHER_DETAILS_LAT,
                WEATHER_DETAILS_LONG
            )
        ).thenReturn(singleApiResponse)

        val coordinates = Coordinates(
            WEATHER_DETAILS_LONG.toDouble(),
            WEATHER_DETAILS_LAT.toDouble()
        )
        val testObserver = weatherRepository.fetchWeatherByCoordinates(coordinates)
            .test()

        verify(weatherApiService).getWeatherByCoordinates(
            WEATHER_DETAILS_LAT,
            WEATHER_DETAILS_LONG,
            WeatherApiService.APP_ID,
            WeatherApiService.DEFAULT_UNITS
        )
        verifyNoMoreInteractions(weatherApiService)
        testObserver.assertValue(getWeatherApiResponse().toWeather())
        testObserver.dispose()
    }

    @Test
    fun `should return weather by location`() {
        val singleApiResponse = Single.fromCallable {
            return@fromCallable Response.success(getWeatherApiResponse())
        }
        `when`(
            weatherApiService.getWeatherByCityName(WEATHER_DETAILS_CITY_NAME)
        ).thenReturn(singleApiResponse)

        val testObserver = weatherRepository.fetchWeatherByCityName(WEATHER_DETAILS_CITY_NAME)
            .test()
        verify(weatherApiService).getWeatherByCityName(
            WEATHER_DETAILS_CITY_NAME,
            WeatherApiService.APP_ID,
            WeatherApiService.DEFAULT_UNITS
        )
        verifyNoMoreInteractions(weatherApiService)
        testObserver.assertValue(getWeatherApiResponse().toWeather())
        testObserver.dispose()
    }

    @Test
    fun `should return locally stored weather list`() {
        val allStoredWeatherList = listOf(getWeatherApiResponse().toWeather())
        val singleRoomResponse = Single.fromCallable {
            return@fromCallable allStoredWeatherList
        }
        `when`(
            weatherDao.getAll()
        ).thenReturn(singleRoomResponse)

        val testObserver = weatherRepository.geLocallyStoredWeatherDetails()
            .test()
        verify(weatherDao).getAll()
        verifyNoMoreInteractions(weatherDao)
        testObserver.assertValue(allStoredWeatherList)
        testObserver.dispose()
    }

    @Test
    fun `should store weather locally`() {
        val weather = getWeatherApiResponse().toWeather()
        val completable = Completable.complete()
        `when`(
            weatherDao.insert(weather)
        ).thenReturn(completable)

        val testObserver = weatherRepository.storeWeatherDetailsLocally(weather)
            .test()
        verify(weatherDao).insert(weather)
        verifyNoMoreInteractions(weatherDao)
        testObserver.dispose()
    }

}