package com.example.weatherlogger.ui

import android.opengl.Visibility
import android.view.View
import android.view.View.*

fun View.visible(){
    visibility = VISIBLE
}

fun View.gone(){
    visibility = GONE
}

fun View.invisible(){
    visibility = INVISIBLE
}