package com.example.weatherlogger

import android.app.Activity
import android.app.Application
import com.example.weatherlogger.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class WeatherLoggerApp : Application(), HasActivityInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

}