package com.example.weatherlogger.di

import android.content.Context
import com.example.weatherlogger.data.repository.WeatherRepository
import com.example.weatherlogger.scheduler.SchedulerService
import com.example.weatherlogger.ui.main.MainActivity
import com.example.weatherlogger.ui.main.MainContract
import com.example.weatherlogger.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideMainActivityPresenter(
        weatherRepository: WeatherRepository,
        schedulerService: SchedulerService
    ): MainContract.Presenter {
        return MainPresenter(weatherRepository, schedulerService)
    }

    @Provides
    fun provideContext(mainActivity: MainActivity): Context {
        return mainActivity
    }
}