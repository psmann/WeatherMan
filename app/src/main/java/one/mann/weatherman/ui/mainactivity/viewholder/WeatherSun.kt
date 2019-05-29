package one.mann.weatherman.ui.mainactivity.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R
import one.mann.weatherman.ui.customview.SunGraphView

internal class WeatherSun(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var sunrise: TextView = itemView.findViewById(R.id.sunrise_result)
    var sunset: TextView = itemView.findViewById(R.id.sunset_result)
    var dayLength: TextView = itemView.findViewById(R.id.day_length_result)
    var sunGraphView: SunGraphView = itemView.findViewById(R.id.sunlight_graph)
}