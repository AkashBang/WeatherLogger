package com.example.weatherlogger.di
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.weatherlogger.WeatherLoggerApp
import dagger.android.AndroidInjection
import dagger.android.HasActivityInjector

object AppInjector {

    fun init(weatherLoggerApp: WeatherLoggerApp) {
        DaggerAppComponent.builder().provideContext(weatherLoggerApp)
            .build().inject(weatherLoggerApp)
        weatherLoggerApp
            .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {

                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

                }

                override fun onActivityDestroyed(activity: Activity) {

                }
            })

    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasActivityInjector) {
            AndroidInjection.inject(activity)
        }
    }
}