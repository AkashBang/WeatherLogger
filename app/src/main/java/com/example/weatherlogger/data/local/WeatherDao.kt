package com.example.weatherlogger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherlogger.domain.model.Weather
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAll(): Single<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: Weather):Completable
}