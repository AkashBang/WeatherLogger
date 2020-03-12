package com.example.weatherlogger.di


import android.app.Application
import android.content.Context
import com.example.weatherlogger.data.local.WeatherDatabase
import com.example.weatherlogger.data.network.WeatherApiService
import com.example.weatherlogger.data.network.WeatherApiService.Companion.BASE_URL
import com.example.weatherlogger.data.repository.WeatherRepository
import com.example.weatherlogger.scheduler.SchedulerService
import com.example.weatherlogger.scheduler.SchedulerServiceImpl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideWeatherApiService(okHttpClient: OkHttpClient): WeatherApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WeatherApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return logging
    }


    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
        context: Application
    ): WeatherRepository {
        return WeatherRepository(
            weatherApiService,
            WeatherDatabase.getInstance(context).weatherDao()
        )
    }

    @Singleton
    @Provides
    fun provideSchedulerService(): SchedulerService {
        return SchedulerServiceImpl()
    }
}