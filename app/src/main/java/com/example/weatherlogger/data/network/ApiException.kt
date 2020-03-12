package com.example.weatherlogger.data.network

class ApiException(
    private val code: Int,
    message: String
) : Exception(message)