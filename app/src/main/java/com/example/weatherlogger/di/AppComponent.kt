package com.example.weatherlogger.di

import android.app.Application
import com.example.weatherlogger.WeatherLoggerApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        AppModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideContext(context: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: WeatherLoggerApp)
}