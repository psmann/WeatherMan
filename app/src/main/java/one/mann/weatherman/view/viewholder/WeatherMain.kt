package one.mann.weatherman.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import one.mann.weatherman.R

class WeatherMain(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var currentTemp: TextView = itemView.findViewById(R.id.current_temp_result)
    internal var feelsLike: TextView = itemView.findViewById(R.id.feels_like_result)
    internal var maxTemp: TextView = itemView.findViewById(R.id.max_temp_result)
    internal var minTemp: TextView = itemView.findViewById(R.id.min_temp_result)
    internal var humidity: TextView = itemView.findViewById(R.id.humidity_result)
    internal var lastUpdated: TextView = itemView.findViewById(R.id.last_updated_result)
    internal var description: TextView = itemView.findViewById(R.id.description)
    internal var weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
}