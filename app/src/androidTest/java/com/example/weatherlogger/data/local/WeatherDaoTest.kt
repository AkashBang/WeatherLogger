package com.example.weatherlogger.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherlogger.domain.model.Weather
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    private lateinit var weatherDao: WeatherDao
    private lateinit var db: WeatherDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java
        ).build()
        weatherDao = db.weatherDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeWeatherAndReadInList() {
        val weather: Weather =
            Weather(
                1,
                "Pune",
                "IN",
                "",
                "description",
                10.00,
                05.00,
                100,
                32,
                0,
                "date"
            )
        weatherDao.insert(weather).blockingAwait()
        val weatherList = weatherDao
            .getAll()
            .test()
            .values()[0]
        assertThat(weatherList[0], Matchers.equalTo(weather))
    }
}