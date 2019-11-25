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
        val cityName: TextView = itemView.findViewById(R.id.current_city_name_text_view)
        val lastChecked: TextView = itemView.findViewById(R.id.current_last_checked_result_text_view)
        val currentTemp: TextView = itemView.findViewById(R.id.current_current_temp_result_text_view)
        val feelsLike: TextView = itemView.findViewById(R.id.current_feels_like_result_text_view)
        val maxTemp: TextView = itemView.findViewById(R.id.current_max_temp_result_text_view)
        val minTemp: TextView = itemView.findViewById(R.id.current_min_temp_result_text_view)
        val description: TextView = itemView.findViewById(R.id.current_description_text_view)
        val weatherIcon: ImageView = itemView.findViewById(R.id.current_weather_icon_image_view)
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
        val sunrise: TextView = itemView.findViewById(R.id.sun_cycle_sunrise_result_text_view)
        val sunset: TextView = itemView.findViewById(R.id.sun_cycle_sunset_result_text_view)
        val dayLength: TextView = itemView.findViewById(R.id.sun_cycle_day_length_result_text_view)
        val sunGraphView: SunGraphView = itemView.findViewById(R.id.sun_cycle_sun_graph_view)
    }

    class HourlyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val forecastGraph: ForecastGraphView = itemView.findViewById(R.id.forecast_hourly_forecast_graph_view)
        val forecast1Time: TextView = itemView.findViewById(R.id.forecast_hourly_1_time_text_view)
        val forecast1Temp: TextView = itemView.findViewById(R.id.forecast_hourly_1_temp_text_view)
        val forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_1_icon_image_view)
        val forecast2Time: TextView = itemView.findViewById(R.id.forecast_hourly_2_time_text_view)
        val forecast2Temp: TextView = itemView.findViewById(R.id.forecast_hourly_2_temp_text_view)
        val forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_2_icon_image_view)
        val forecast3Time: TextView = itemView.findViewById(R.id.forecast_hourly_3_time_text_view)
        val forecast3Temp: TextView = itemView.findViewById(R.id.forecast_hourly_3_temp_text_view)
        val forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_3_icon_image_view)
        val forecast4Time: TextView = itemView.findViewById(R.id.forecast_hourly_4_time_text_view)
        val forecast4Temp: TextView = itemView.findViewById(R.id.forecast_hourly_4_temp_text_view)
        val forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_4_icon_image_view)
        val forecast5Time: TextView = itemView.findViewById(R.id.forecast_hourly_5_time_text_view)
        val forecast5Temp: TextView = itemView.findViewById(R.id.forecast_hourly_5_temp_text_view)
        val forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_5_icon_image_view)
        val forecast6Time: TextView = itemView.findViewById(R.id.forecast_hourly_6_time_text_view)
        val forecast6Temp: TextView = itemView.findViewById(R.id.forecast_hourly_6_temp_text_view)
        val forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_6_icon_image_view)
        val forecast7Time: TextView = itemView.findViewById(R.id.forecast_hourly_7_time_text_view)
        val forecast7Temp: TextView = itemView.findViewById(R.id.forecast_hourly_7_temp_text_view)
        val forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_hourly_7_icon_image_view)
    }

    class DailyForecast(itemView: View) : WeatherViewHolder(itemView) {
        val forecast1Day: TextView = itemView.findViewById(R.id.forecast_daily_1_day_text_view)
        val forecast1Min: TextView = itemView.findViewById(R.id.forecast_daily_1_min_text_view)
        val forecast1Max: TextView = itemView.findViewById(R.id.forecast_daily_1_max_text_view)
        val forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_daily_1_icon_image_view)
        val forecast2Day: TextView = itemView.findViewById(R.id.forecast_daily_2_day_text_view)
        val forecast2Min: TextView = itemView.findViewById(R.id.forecast_daily_2_min_text_view)
        val forecast2Max: TextView = itemView.findViewById(R.id.forecast_daily_2_max_text_view)
        val forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_daily_2_icon_image_view)
        val forecast3Day: TextView = itemView.findViewById(R.id.forecast_daily_3_day_text_view)
        val forecast3Min: TextView = itemView.findViewById(R.id.forecast_daily_3_min_text_view)
        val forecast3Max: TextView = itemView.findViewById(R.id.forecast_daily_3_max_text_view)
        val forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_daily_3_icon_image_view)
        val forecast4Day: TextView = itemView.findViewById(R.id.forecast_daily_4_day_text_view)
        val forecast4Min: TextView = itemView.findViewById(R.id.forecast_daily_4_min_text_view)
        val forecast4Max: TextView = itemView.findViewById(R.id.forecast_daily_4_max_text_view)
        val forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_daily_4_icon_image_view)
        val forecast5Day: TextView = itemView.findViewById(R.id.forecast_daily_5_day_text_view)
        val forecast5Min: TextView = itemView.findViewById(R.id.forecast_daily_5_min_text_view)
        val forecast5Max: TextView = itemView.findViewById(R.id.forecast_daily_5_max_text_view)
        val forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_daily_5_icon_image_view)
        val forecast6Day: TextView = itemView.findViewById(R.id.forecast_daily_6_day_text_view)
        val forecast6Min: TextView = itemView.findViewById(R.id.forecast_daily_6_min_text_view)
        val forecast6Max: TextView = itemView.findViewById(R.id.forecast_daily_6_max_text_view)
        val forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_daily_6_icon_image_view)
        val forecast7Day: TextView = itemView.findViewById(R.id.forecast_daily_7_day_text_view)
        val forecast7Min: TextView = itemView.findViewById(R.id.forecast_daily_7_min_text_view)
        val forecast7Max: TextView = itemView.findViewById(R.id.forecast_daily_7_max_text_view)
        val forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_daily_7_icon_image_view)
    }
}