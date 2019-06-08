package one.mann.weatherman.ui.main.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R

internal class WeatherMain(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var currentTemp: TextView = itemView.findViewById(R.id.current_temp_result)
    var feelsLike: TextView = itemView.findViewById(R.id.feels_like_result)
    var maxTemp: TextView = itemView.findViewById(R.id.max_temp_result)
    var minTemp: TextView = itemView.findViewById(R.id.min_temp_result)
    var humidity: TextView = itemView.findViewById(R.id.humidity_result)
    var lastUpdated: TextView = itemView.findViewById(R.id.last_updated_result)
    var description: TextView = itemView.findViewById(R.id.description)
    var weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
}