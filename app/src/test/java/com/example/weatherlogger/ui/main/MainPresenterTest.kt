package com.example.weatherlogger.ui.main

import com.example.weatherlogger.WEATHER_DETAILS_LAT
import com.example.weatherlogger.WEATHER_DETAILS_LONG
import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.data.repository.WeatherRepository
import com.example.weatherlogger.domain.model.Weather
import com.example.weatherlogger.getWeatherApiResponse
import com.example.weatherlogger.scheduler.SchedulerService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.net.UnknownHostException

class MainPresenterTest {

    private val view = mock(MainContract.View::class.java)
    lateinit var schedulerService: SchedulerService
    private val weatherRepository = mock(WeatherRepository::class.java)
    lateinit var mainPresenter: MainPresenter

    @Before
    fun setUp() {
        schedulerService = object : SchedulerService {
            override fun io() = Schedulers.trampoline()
            override fun main() = Schedulers.trampoline()
            override fun computation() = Schedulers.trampoline()
        }
        mainPresenter = MainPresenter(weatherRepository, schedulerService)
    }

    @Test
    fun `test show weather by location`() {
        val weather = getWeatherApiResponse().toWeather()
        val weatherList = listOf(weather)

        val formattedApiResponse: Single<Weather> = Single.create {
            it.onSuccess(weather)
        }

        val databaseResponse: Single<List<Weather>> = Single.create {
            it.onSuccess(weatherList)
        }

        val coordinates = Coordinates(
            WEATHER_DETAILS_LONG.toDouble(),
            WEATHER_DETAILS_LAT.toDouble()
        )

        `when`(weatherRepository.fetchWeatherByCoordinates(coordinates))
            .thenReturn(formattedApiResponse)
        `when`(weatherRepository.storeWeatherDetailsLocally(weather))
            .thenReturn(Completable.complete())
        `when`(weatherRepository.geLocallyStoredWeatherDetails())
            .thenReturn(databaseResponse)

        mainPresenter.attachView(view)
        mainPresenter.getWeatherDetailsByCoordinates(coordinates)
        verify(view).apply {
            view.showWeatherDetails(weatherList)
        }
    }

    @Test
    fun `test show weather by city name`() {
        val weather = getWeatherApiResponse().toWeather()
        val weatherList = listOf(weather)

        val formattedApiResponse: Single<Weather> = Single.create {
            it.onSuccess(weather)
        }

        val databaseResponse: Single<List<Weather>> = Single.create {
            it.onSuccess(weatherList)
        }
        val cityName = "Pune"
        `when`(weatherRepository.fetchWeatherByCityName(cityName))
            .thenReturn(formattedApiResponse)
        `when`(weatherRepository.storeWeatherDetailsLocally(weather))
            .thenReturn(Completable.complete())
        `when`(weatherRepository.geLocallyStoredWeatherDetails())
            .thenReturn(databaseResponse)

        mainPresenter.attachView(view)
        mainPresenter.getWeatherDetailsByCityName(cityName)
        verify(view).apply {
            view.showLoading()
            view.hideLoading()
            view.showWeatherDetails(weatherList)
        }
    }

    @Test
    fun `test show generic error`() {
        val errorMessage = "error"
        val formattedApiResponse: Single<Weather> = Single.create {
            it.onError(Exception(errorMessage))
        }
        val databaseResponse: Single<List<Weather>> = Single.create {
            it.onError(Exception(errorMessage))
        }
        val cityName = "Pune"
        `when`(weatherRepository.fetchWeatherByCityName(cityName))
            .thenReturn(formattedApiResponse)
               `when`(weatherRepository.geLocallyStoredWeatherDetails())
            .thenReturn(databaseResponse)
        mainPresenter.attachView(view)
        mainPresenter.getWeatherDetailsByCityName(cityName)
        verify(view).apply {
            view.showLoading()
            view.hideLoading()
            view.showError(errorMessage)
        }
    }

    @Test
    fun `test show connectivity error`() {
        val errorMessage = "there is network issue"
        val formattedApiResponse: Single<Weather> = Single.create {
            it.onError(UnknownHostException(errorMessage))
        }
        val databaseResponse: Single<List<Weather>> = Single.create {
            it.onError(Exception(errorMessage))
        }
        val cityName = "Pune"
        `when`(weatherRepository.fetchWeatherByCityName(cityName))
            .thenReturn(formattedApiResponse)
        `when`(weatherRepository.geLocallyStoredWeatherDetails())
            .thenReturn(databaseResponse)

        mainPresenter.attachView(view)
        mainPresenter.getWeatherDetailsByCityName(cityName)
        verify(view).apply {
            view.showLoading()
            view.hideLoading()
            view.showError(errorMessage)
        }
    }
}