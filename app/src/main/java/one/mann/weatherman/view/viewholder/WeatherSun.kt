package one.mann.weatherman.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import one.mann.weatherman.R

class WeatherSun(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var sunrise: TextView = itemView.findViewById(R.id.sunrise_result)
    internal var sunset: TextView = itemView.findViewById(R.id.sunset_result)
    internal var dayLength: TextView = itemView.findViewById(R.id.day_length_result)
}