package one.mann.weatherman.ui.detail.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.databinding.*

/* Created by Psmann. */

internal sealed class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Current(itemView: View) : WeatherViewHolder(itemView) {
        val binding: ItemWeatherCurrentBinding = ItemWeatherCurrentBinding.bind(itemView)
    }

    class Conditions(itemView: View) : WeatherViewHolder(itemView) {
        val binding: ItemWeatherConditionsBinding = ItemWeatherConditionsBinding.bind(itemView)
    }

    class SunCycle(itemView: View) : WeatherViewHolder(itemView) {
        val binding: ItemWeatherSunCycleBinding = ItemWeatherSunCycleBinding.bind(itemView)
    }

    class HourlyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val binding: ItemWeatherForecastHourlyBinding = ItemWeatherForecastHourlyBinding.bind(itemView)
    }

    class DailyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val binding: ItemWeatherForecastDailyBinding = ItemWeatherForecastDailyBinding.bind(itemView)
    }
}