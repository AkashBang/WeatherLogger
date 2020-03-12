package com.example.weatherlogger.ui.main

import com.example.weatherlogger.EMPTY_STRING
import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.data.repository.WeatherRepository
import com.example.weatherlogger.scheduler.SchedulerService
import com.example.weatherlogger.ui.base.BasePresenterImpl
import io.reactivex.rxkotlin.addTo

class MainPresenter(
    private val weatherRepository: WeatherRepository,
    private val schedulerService: SchedulerService
) : MainContract.Presenter,
    BasePresenterImpl<MainContract.View>() {

    override fun getWeatherDetailsByCoordinates(coordinates: Coordinates) {
        weatherRepository.fetchWeatherByCoordinates(coordinates)
            .subscribeOn(schedulerService.io())
            .doOnSubscribe {
                view?.showLoading()
            }.flatMapCompletable {
                //Store the data in local db
                weatherRepository.storeWeatherDetailsLocally(it)
            }.andThen(
                //Get the data from local db
                weatherRepository.geLocallyStoredWeatherDetails()
            )
            .observeOn(schedulerService.main())

            .subscribe({
                view?.hideLoading()
                view?.showWeatherDetails(it)
            }, {
                view?.hideLoading()
                handleError(it, {
                    view?.showError(it.message ?: EMPTY_STRING)
                }, showConnectivityError = true)
            }).addTo(compositeDisposable)
    }

    override fun getWeatherDetailsByCityName(cityName: String) {
        weatherRepository.fetchWeatherByCityName(cityName)
            .subscribeOn(schedulerService.io())
            .doOnSubscribe {
                view?.showLoading()
            }.flatMapCompletable {
                //Store the data in local db
                weatherRepository.storeWeatherDetailsLocally(it)
            }.andThen(
                //Get the data from local db
                weatherRepository.geLocallyStoredWeatherDetails()
            )
            .observeOn(schedulerService.main())

            .subscribe({
                view?.hideLoading()
                view?.showWeatherDetails(it)
            }, {
                view?.hideLoading()
                handleError(it, {
                    view?.showError(it.message ?: EMPTY_STRING)
                }, showConnectivityError = true)
            }).addTo(compositeDisposable)
    }

    override fun getStoredWeatherDetails() {
        weatherRepository.geLocallyStoredWeatherDetails()
            .subscribeOn(schedulerService.io())
            .observeOn(schedulerService.main())
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.hideLoading() }
            .subscribe({
                view?.showWeatherDetails(it)
            }, {
                handleError(it, {
                    view?.showError(it.message ?: EMPTY_STRING)
                }, showConnectivityError = true)

            }).addTo(compositeDisposable)
    }
}