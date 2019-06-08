package one.mann.weatherman.ui.main.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R

internal class WeatherClouds(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var clouds: TextView = itemView.findViewById(R.id.clouds_result)
    var windSpeed: TextView = itemView.findViewById(R.id.wind_speed_result)
    var windDirection: TextView = itemView.findViewById(R.id.wind_direction_result)
    var pressure: TextView = itemView.findViewById(R.id.pressure_result)
    var visibility: TextView = itemView.findViewById(R.id.visibility_result)
}