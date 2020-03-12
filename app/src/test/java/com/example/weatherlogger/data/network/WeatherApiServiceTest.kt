package com.example.weatherlogger.data.network

import com.example.weatherlogger.WEATHER_DETAILS_API_RESPONSE
import com.example.weatherlogger.WEATHER_DETAILS_CITY_NAME
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiServiceTest {

    private lateinit var service: WeatherApiService
    private var mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    @Test
    fun `test get weather by location api`() {
        enqueueResponse(WEATHER_DETAILS_API_RESPONSE)
        val latitude = "18.59"
        val longitude = "19.49"
        val testObserver = service.getWeatherByCoordinates(
            latitude,
            longitude,
            WeatherApiService.APP_ID,
            WeatherApiService.DEFAULT_UNITS
        ).test()
        val request = mockWebServer.takeRequest()
        val expectedEndpoint = WeatherApiService.GET_WEATHER_ENDPOINT +
                "?lat=$latitude" +
                "&lon=$longitude" +
                "&appid=${WeatherApiService.APP_ID}" +
                "&units=${WeatherApiService.DEFAULT_UNITS}"
        assertEquals(
            expectedEndpoint,
            request.path
        )
        val weather = testObserver.values()[0]
        assertEquals(WEATHER_DETAILS_CITY_NAME, weather.body()?.name)
        testObserver.dispose()
    }

    @Test
    fun `test get weather by city name api`() {
        enqueueResponse(WEATHER_DETAILS_API_RESPONSE)
        val testObserver = service.getWeatherByCityName(
            WEATHER_DETAILS_CITY_NAME,
            WeatherApiService.APP_ID,
            WeatherApiService.DEFAULT_UNITS
        ).test()
        val request = mockWebServer.takeRequest()
        val expectedEndpoint = WeatherApiService.GET_WEATHER_ENDPOINT +
                "?q=$WEATHER_DETAILS_CITY_NAME" +
                "&appid=${WeatherApiService.APP_ID}" +
                "&units=${WeatherApiService.DEFAULT_UNITS}"
        assertEquals(
            expectedEndpoint,
            request.path
        )
        val weather = testObserver.values()[0]
        assertEquals(WEATHER_DETAILS_CITY_NAME, weather.body()?.name)
        testObserver.dispose()
    }

    private fun enqueueResponse(response: String, headers: Map<String, String> = emptyMap()) {
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(response)
        )
    }
}