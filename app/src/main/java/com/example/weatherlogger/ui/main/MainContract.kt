package com.example.weatherlogger.ui.main

import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.domain.model.Weather
import com.example.weatherlogger.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.BaseView {
        fun showWeatherDetails(weatherList:List<Weather>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun getWeatherDetailsByCoordinates(coordinates: Coordinates)
        fun getWeatherDetailsByCityName(cityName: String)
        fun getStoredWeatherDetails()
    }
}
