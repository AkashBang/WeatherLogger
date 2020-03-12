package com.example.weatherlogger.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.domain.model.Weather
import com.example.weatherlogger.ui.details.WeatherDetailsActivity
import kotlinx.android.synthetic.main.single_row_weather.view.*

class WeatherDetailsAdapter(private var weatherList: List<Weather>) :
    RecyclerView.Adapter<WeatherDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.single_row_weather, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val currentTemp = "${weatherList[position].currentTemp} °C"
            cityName.text = "${weatherList[position].name}, ${weatherList[position].country}"
            temperature.text = currentTemp
            date.text = weatherList[position].date
            setOnClickListener {
                WeatherDetailsActivity.start(weatherList[holder.layoutPosition],context = context)
            }
        }
    }

    fun setData(weatherList: List<Weather>) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}