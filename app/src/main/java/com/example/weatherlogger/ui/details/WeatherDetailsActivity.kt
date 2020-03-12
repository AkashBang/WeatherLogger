package com.example.weatherlogger.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.weatherlogger.R
import com.example.weatherlogger.domain.model.Weather
import kotlinx.android.synthetic.main.activity_weather_details.*


class WeatherDetailsActivity : AppCompatActivity() {

    private val weather: Weather? by lazy {
        intent.getParcelableExtra(WEATHER_EXTRA_KEY) as Weather
    }

    companion object {
        private const val WEATHER_EXTRA_KEY = "weatherExtra"
        fun start(weather: Weather, context: Context) {
            val bundle = ActivityOptionsCompat.makeCustomAnimation(
                context,
                android.R.anim.fade_in, android.R.anim.fade_out
            ).toBundle()
            val intent = Intent(context, WeatherDetailsActivity::class.java).apply {
                putExtra(WEATHER_EXTRA_KEY, weather)
            }
            context.startActivity(intent, bundle)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)
        setUpUi()
    }

    private fun setUpUi() {
        setToolbar()
        weather?.apply {
            val name = "$name, $country"
            val currentTemp = "$currentTemp °C"
            val feelsLike = "$feelsLike °C"
            val pressure = "$pressure hPa"
            val humidity = "$humidity %"

            nameTv.text = name
            descriptionTv.text = description.capitalize()
            temperatureTv.text = currentTemp
            feelsLikeTv.text = feelsLike
            pressureTv.text = pressure
            humidityTv.text = humidity
            dateTv.text = date
        }
    }

    private fun setToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.title_weather_details)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
