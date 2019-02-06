package one.mann.weatherman.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import one.mann.weatherman.R

class WeatherCity(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var cityName: TextView = itemView.findViewById(R.id.city_name)
    internal var flagIcon: TextView = itemView.findViewById(R.id.country_flag)
    internal var lastChecked: TextView = itemView.findViewById(R.id.last_checked_result)
    internal var location: TextView = itemView.findViewById(R.id.location_result)
}