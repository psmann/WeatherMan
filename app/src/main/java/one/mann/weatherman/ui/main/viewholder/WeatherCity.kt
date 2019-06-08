package one.mann.weatherman.ui.main.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R

internal class WeatherCity(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cityName: TextView = itemView.findViewById(R.id.city_name)
    var flagIcon: TextView = itemView.findViewById(R.id.country_flag)
    var lastChecked: TextView = itemView.findViewById(R.id.last_checked_result)
    var location: TextView = itemView.findViewById(R.id.location_result)
}