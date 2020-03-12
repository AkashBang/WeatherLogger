package com.example.weatherlogger.data.network

import retrofit2.Response
import java.util.*

fun <T> Response<T>.getBody(): T? {

    return if (isSuccessful) body()
    else throw ApiException(code(), message())
}