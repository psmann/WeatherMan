package one.mann.weatherman.ui.detail.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R
import one.mann.weatherman.ui.main.SunGraphView

internal sealed class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class City(itemView: View) : WeatherViewHolder(itemView) {
        var cityName: TextView = itemView.findViewById(R.id.city_name)
        var flagIcon: TextView = itemView.findViewById(R.id.country_flag)
        var lastChecked: TextView = itemView.findViewById(R.id.last_checked_result)
        var location: TextView = itemView.findViewById(R.id.location_result)
    }

    class Main(itemView: View) : WeatherViewHolder(itemView) {
        var currentTemp: TextView = itemView.findViewById(R.id.current_temp_result)
        var feelsLike: TextView = itemView.findViewById(R.id.feels_like_result)
        var maxTemp: TextView = itemView.findViewById(R.id.max_temp_result)
        var minTemp: TextView = itemView.findViewById(R.id.min_temp_result)
        var humidity: TextView = itemView.findViewById(R.id.humidity_result)
        var lastUpdated: TextView = itemView.findViewById(R.id.last_updated_result)
        var description: TextView = itemView.findViewById(R.id.description)
        var weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
    }

    class Sun(itemView: View) : WeatherViewHolder(itemView) {
        var sunrise: TextView = itemView.findViewById(R.id.sunrise_result)
        var sunset: TextView = itemView.findViewById(R.id.sunset_result)
        var dayLength: TextView = itemView.findViewById(R.id.day_length_result)
        var sunGraphView: SunGraphView = itemView.findViewById(R.id.sunlight_graph)
    }

    class Clouds(itemView: View) : WeatherViewHolder(itemView) {
        var clouds: TextView = itemView.findViewById(R.id.clouds_result)
        var windSpeed: TextView = itemView.findViewById(R.id.wind_speed_result)
        var windDirection: TextView = itemView.findViewById(R.id.wind_direction_result)
        var pressure: TextView = itemView.findViewById(R.id.pressure_result)
        var visibility: TextView = itemView.findViewById(R.id.visibility_result)
    }

    class DailyForecast(itemView: View) : WeatherViewHolder(itemView) {
        var forecast1Day: TextView = itemView.findViewById(R.id.forecast_1_day)
        var forecast1Min: TextView = itemView.findViewById(R.id.forecast_1_min)
        var forecast1Max: TextView = itemView.findViewById(R.id.forecast_1_max)
        var forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
        var forecast2Day: TextView = itemView.findViewById(R.id.forecast_2_day)
        var forecast2Min: TextView = itemView.findViewById(R.id.forecast_2_min)
        var forecast2Max: TextView = itemView.findViewById(R.id.forecast_2_max)
        var forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
        var forecast3Day: TextView = itemView.findViewById(R.id.forecast_3_day)
        var forecast3Min: TextView = itemView.findViewById(R.id.forecast_3_min)
        var forecast3Max: TextView = itemView.findViewById(R.id.forecast_3_max)
        var forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
        var forecast4Day: TextView = itemView.findViewById(R.id.forecast_4_day)
        var forecast4Min: TextView = itemView.findViewById(R.id.forecast_4_min)
        var forecast4Max: TextView = itemView.findViewById(R.id.forecast_4_max)
        var forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
        var forecast5Day: TextView = itemView.findViewById(R.id.forecast_5_day)
        var forecast5Min: TextView = itemView.findViewById(R.id.forecast_5_min)
        var forecast5Max: TextView = itemView.findViewById(R.id.forecast_5_max)
        var forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
        var forecast6Day: TextView = itemView.findViewById(R.id.forecast_6_day)
        var forecast6Min: TextView = itemView.findViewById(R.id.forecast_6_min)
        var forecast6Max: TextView = itemView.findViewById(R.id.forecast_6_max)
        var forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
        var forecast7Day: TextView = itemView.findViewById(R.id.forecast_7_day)
        var forecast7Min: TextView = itemView.findViewById(R.id.forecast_7_min)
        var forecast7Max: TextView = itemView.findViewById(R.id.forecast_7_max)
        var forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
    }

    class HourlyForecast(itemView: View) : WeatherViewHolder(itemView) {
        var forecast1Time: TextView = itemView.findViewById(R.id.forecast_1_time)
        var forecast1Temp: TextView = itemView.findViewById(R.id.forecast_1_temp)
        var forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
        var forecast2Time: TextView = itemView.findViewById(R.id.forecast_2_time)
        var forecast2Temp: TextView = itemView.findViewById(R.id.forecast_2_temp)
        var forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
        var forecast3Time: TextView = itemView.findViewById(R.id.forecast_3_time)
        var forecast3Temp: TextView = itemView.findViewById(R.id.forecast_3_temp)
        var forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
        var forecast4Time: TextView = itemView.findViewById(R.id.forecast_4_time)
        var forecast4Temp: TextView = itemView.findViewById(R.id.forecast_4_temp)
        var forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
        var forecast5Time: TextView = itemView.findViewById(R.id.forecast_5_time)
        var forecast5Temp: TextView = itemView.findViewById(R.id.forecast_5_temp)
        var forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
        var forecast6Time: TextView = itemView.findViewById(R.id.forecast_6_time)
        var forecast6Temp: TextView = itemView.findViewById(R.id.forecast_6_temp)
        var forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
        var forecast7Time: TextView = itemView.findViewById(R.id.forecast_7_time)
        var forecast7Temp: TextView = itemView.findViewById(R.id.forecast_7_temp)
        var forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
    }
}