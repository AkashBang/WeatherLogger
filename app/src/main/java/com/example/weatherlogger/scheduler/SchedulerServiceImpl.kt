package com.example.weatherlogger.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerServiceImpl: SchedulerService {

    override fun io() = Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation() = Schedulers.computation()
}