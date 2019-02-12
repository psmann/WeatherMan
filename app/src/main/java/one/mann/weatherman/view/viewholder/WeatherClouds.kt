package one.mann.weatherman.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import one.mann.weatherman.R

class WeatherClouds(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var clouds: TextView = itemView.findViewById(R.id.clouds_result)
    internal var windSpeed: TextView = itemView.findViewById(R.id.wind_speed_result)
    internal var windDirection: TextView = itemView.findViewById(R.id.wind_direction_result)
    internal var pressure: TextView = itemView.findViewById(R.id.pressure_result)
    internal var visibility: TextView = itemView.findViewById(R.id.visibility_result)
}