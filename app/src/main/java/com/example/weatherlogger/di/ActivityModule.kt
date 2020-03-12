package com.example.weatherlogger.di

import com.example.weatherlogger.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun injectMainActivity(): MainActivity
}