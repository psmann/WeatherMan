package one.mann.weatherman.ui.detail.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.databinding.*

/* Created by Psmann. */

internal sealed class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Current(
            itemView: View,
            val binding: ItemWeatherCurrentBinding = ItemWeatherCurrentBinding.bind(itemView)
    ) : WeatherViewHolder(itemView)

    class Conditions(
            itemView: View,
            val binding: ItemWeatherConditionsBinding = ItemWeatherConditionsBinding.bind(itemView)
    ) : WeatherViewHolder(itemView)

    class SunCycle(
            itemView: View,
            val binding: ItemWeatherSunCycleBinding = ItemWeatherSunCycleBinding.bind(itemView)
    ) : WeatherViewHolder(itemView)

    class HourlyForecast(
            itemView: View,
            val binding: ItemWeatherForecastHourlyBinding = ItemWeatherForecastHourlyBinding.bind(itemView)
    ) : WeatherViewHolder(itemView)

    class DailyForecast(
            itemView: View,
            val binding: ItemWeatherForecastDailyBinding = ItemWeatherForecastDailyBinding.bind(itemView)
    ) : WeatherViewHolder(itemView)
}