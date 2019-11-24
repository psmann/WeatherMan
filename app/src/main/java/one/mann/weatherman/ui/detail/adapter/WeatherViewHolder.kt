package one.mann.weatherman.ui.detail.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R
import one.mann.weatherman.ui.detail.views.ForecastGraphView
import one.mann.weatherman.ui.detail.views.SunGraphView

internal sealed class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Current(itemView: View) : WeatherViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.city_name_text_view)
        val lastChecked: TextView = itemView.findViewById(R.id.last_checked_result)
        val currentTemp: TextView = itemView.findViewById(R.id.current_temp_result)
        val feelsLike: TextView = itemView.findViewById(R.id.feels_like_result)
        val maxTemp: TextView = itemView.findViewById(R.id.max_temp_result)
        val minTemp: TextView = itemView.findViewById(R.id.min_temp_result)
        val description: TextView = itemView.findViewById(R.id.description)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon_image_view)
    }

    class Conditions(itemView: View) : WeatherViewHolder(itemView) {
        val clouds: TextView = itemView.findViewById(R.id.conditions_clouds_result_text_view)
        val windSpeed: TextView = itemView.findViewById(R.id.conditions_wind_speed_result_text_view)
        val windDirection: TextView = itemView.findViewById(R.id.conditions_wind_direction_result_text_view)
        val windDirectionIcon: ImageView = itemView.findViewById(R.id.conditions_wind_dir_icon_image_view)
        val pressure: TextView = itemView.findViewById(R.id.conditions_pressure_result_text_view)
        val visibility: TextView = itemView.findViewById(R.id.conditions_visibility_result_text_view)
        val flagIcon: TextView = itemView.findViewById(R.id.conditions_country_flag_text_view)
        val location: TextView = itemView.findViewById(R.id.conditions_location_result_text_view)
        val lastUpdated: TextView = itemView.findViewById(R.id.conditions_last_updated_result_text_view)
        val humidity: TextView = itemView.findViewById(R.id.conditions_humidity_result_text_view)
    }

    class SunCycle(itemView: View) : WeatherViewHolder(itemView) {
        val sunrise: TextView = itemView.findViewById(R.id.sunrise_result)
        val sunset: TextView = itemView.findViewById(R.id.sunset_result)
        val dayLength: TextView = itemView.findViewById(R.id.day_length_result)
        val sunGraphView: SunGraphView = itemView.findViewById(R.id.sunlight_graph)
    }

    class HourlyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val forecastGraph: ForecastGraphView = itemView.findViewById(R.id.forecast_graph)
        val forecast1Time: TextView = itemView.findViewById(R.id.forecast_1_time)
        val forecast1Temp: TextView = itemView.findViewById(R.id.forecast_1_temp)
        val forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
        val forecast2Time: TextView = itemView.findViewById(R.id.forecast_2_time)
        val forecast2Temp: TextView = itemView.findViewById(R.id.forecast_2_temp)
        val forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
        val forecast3Time: TextView = itemView.findViewById(R.id.forecast_3_time)
        val forecast3Temp: TextView = itemView.findViewById(R.id.forecast_3_temp)
        val forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
        val forecast4Time: TextView = itemView.findViewById(R.id.forecast_4_time)
        val forecast4Temp: TextView = itemView.findViewById(R.id.forecast_4_temp)
        val forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
        val forecast5Time: TextView = itemView.findViewById(R.id.forecast_5_time)
        val forecast5Temp: TextView = itemView.findViewById(R.id.forecast_5_temp)
        val forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
        val forecast6Time: TextView = itemView.findViewById(R.id.forecast_6_time)
        val forecast6Temp: TextView = itemView.findViewById(R.id.forecast_6_temp)
        val forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
        val forecast7Time: TextView = itemView.findViewById(R.id.forecast_7_time)
        val forecast7Temp: TextView = itemView.findViewById(R.id.forecast_7_temp)
        val forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
    }

    class DailyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val forecast1Day: TextView = itemView.findViewById(R.id.forecast_1_day)
        val forecast1Min: TextView = itemView.findViewById(R.id.forecast_1_min)
        val forecast1Max: TextView = itemView.findViewById(R.id.forecast_1_max)
        val forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
        val forecast2Day: TextView = itemView.findViewById(R.id.forecast_2_day)
        val forecast2Min: TextView = itemView.findViewById(R.id.forecast_2_min)
        val forecast2Max: TextView = itemView.findViewById(R.id.forecast_2_max)
        val forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
        val forecast3Day: TextView = itemView.findViewById(R.id.forecast_3_day)
        val forecast3Min: TextView = itemView.findViewById(R.id.forecast_3_min)
        val forecast3Max: TextView = itemView.findViewById(R.id.forecast_3_max)
        val forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
        val forecast4Day: TextView = itemView.findViewById(R.id.forecast_4_day)
        val forecast4Min: TextView = itemView.findViewById(R.id.forecast_4_min)
        val forecast4Max: TextView = itemView.findViewById(R.id.forecast_4_max)
        val forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
        val forecast5Day: TextView = itemView.findViewById(R.id.forecast_5_day)
        val forecast5Min: TextView = itemView.findViewById(R.id.forecast_5_min)
        val forecast5Max: TextView = itemView.findViewById(R.id.forecast_5_max)
        val forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
        val forecast6Day: TextView = itemView.findViewById(R.id.forecast_6_day)
        val forecast6Min: TextView = itemView.findViewById(R.id.forecast_6_min)
        val forecast6Max: TextView = itemView.findViewById(R.id.forecast_6_max)
        val forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
        val forecast7Day: TextView = itemView.findViewById(R.id.forecast_7_day)
        val forecast7Min: TextView = itemView.findViewById(R.id.forecast_7_min)
        val forecast7Max: TextView = itemView.findViewById(R.id.forecast_7_max)
        val forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
    }
}