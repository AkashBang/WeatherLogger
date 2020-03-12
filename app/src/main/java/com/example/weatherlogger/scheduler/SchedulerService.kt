package com.example.weatherlogger.scheduler

import io.reactivex.Scheduler

interface SchedulerService {

    fun io(): Scheduler
    fun main(): Scheduler
    fun computation(): Scheduler
}