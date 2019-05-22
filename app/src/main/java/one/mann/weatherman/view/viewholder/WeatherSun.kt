package one.mann.weatherman.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import one.mann.weatherman.R
import one.mann.weatherman.view.SunlightGraph

class WeatherSun(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var sunrise: TextView = itemView.findViewById(R.id.sunrise_result)
    internal var sunset: TextView = itemView.findViewById(R.id.sunset_result)
    internal var dayLength: TextView = itemView.findViewById(R.id.day_length_result)
    internal var sunIcon: ImageView = itemView.findViewById(R.id.sun_icon)
    internal var sunGraph: SunlightGraph = itemView.findViewById(R.id.sunlight_graph)
    internal var graphStartPoint: View = itemView.findViewById(R.id.graph_start)
    internal var graphEndPoint: View = itemView.findViewById(R.id.graph_end)
}