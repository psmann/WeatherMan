package one.mann.weatherman.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import one.mann.weatherman.R

class WeatherForecast(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var forecast1Day: TextView = itemView.findViewById(R.id.forecast_1_day)
    internal var forecast1Min: TextView = itemView.findViewById(R.id.forecast_1_min)
    internal var forecast1Max: TextView = itemView.findViewById(R.id.forecast_1_max)
    internal var forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
    internal var forecast2Day: TextView = itemView.findViewById(R.id.forecast_2_day)
    internal var forecast2Min: TextView = itemView.findViewById(R.id.forecast_2_min)
    internal var forecast2Max: TextView = itemView.findViewById(R.id.forecast_2_max)
    internal var forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
    internal var forecast3Day: TextView = itemView.findViewById(R.id.forecast_3_day)
    internal var forecast3Min: TextView = itemView.findViewById(R.id.forecast_3_min)
    internal var forecast3Max: TextView = itemView.findViewById(R.id.forecast_3_max)
    internal var forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
    internal var forecast4Day: TextView = itemView.findViewById(R.id.forecast_4_day)
    internal var forecast4Min: TextView = itemView.findViewById(R.id.forecast_4_min)
    internal var forecast4Max: TextView = itemView.findViewById(R.id.forecast_4_max)
    internal var forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
    internal var forecast5Day: TextView = itemView.findViewById(R.id.forecast_5_day)
    internal var forecast5Min: TextView = itemView.findViewById(R.id.forecast_5_min)
    internal var forecast5Max: TextView = itemView.findViewById(R.id.forecast_5_max)
    internal var forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
    internal var forecast6Day: TextView = itemView.findViewById(R.id.forecast_6_day)
    internal var forecast6Min: TextView = itemView.findViewById(R.id.forecast_6_min)
    internal var forecast6Max: TextView = itemView.findViewById(R.id.forecast_6_max)
    internal var forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
    internal var forecast7Day: TextView = itemView.findViewById(R.id.forecast_7_day)
    internal var forecast7Min: TextView = itemView.findViewById(R.id.forecast_7_min)
    internal var forecast7Max: TextView = itemView.findViewById(R.id.forecast_7_max)
    internal var forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
}