package one.mann.weatherman.ui.main.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import one.mann.weatherman.R

internal class WeatherForecast(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var forecast1Day: TextView = itemView.findViewById(R.id.forecast_1_day)
    var forecast1Min: TextView = itemView.findViewById(R.id.forecast_1_min)
    var forecast1Max: TextView = itemView.findViewById(R.id.forecast_1_max)
    var forecast1Icon: ImageView = itemView.findViewById(R.id.forecast_1_icon)
    var forecast2Day: TextView = itemView.findViewById(R.id.forecast_2_day)
    var forecast2Min: TextView = itemView.findViewById(R.id.forecast_2_min)
    var forecast2Max: TextView = itemView.findViewById(R.id.forecast_2_max)
    var forecast2Icon: ImageView = itemView.findViewById(R.id.forecast_2_icon)
    var forecast3Day: TextView = itemView.findViewById(R.id.forecast_3_day)
    var forecast3Min: TextView = itemView.findViewById(R.id.forecast_3_min)
    var forecast3Max: TextView = itemView.findViewById(R.id.forecast_3_max)
    var forecast3Icon: ImageView = itemView.findViewById(R.id.forecast_3_icon)
    var forecast4Day: TextView = itemView.findViewById(R.id.forecast_4_day)
    var forecast4Min: TextView = itemView.findViewById(R.id.forecast_4_min)
    var forecast4Max: TextView = itemView.findViewById(R.id.forecast_4_max)
    var forecast4Icon: ImageView = itemView.findViewById(R.id.forecast_4_icon)
    var forecast5Day: TextView = itemView.findViewById(R.id.forecast_5_day)
    var forecast5Min: TextView = itemView.findViewById(R.id.forecast_5_min)
    var forecast5Max: TextView = itemView.findViewById(R.id.forecast_5_max)
    var forecast5Icon: ImageView = itemView.findViewById(R.id.forecast_5_icon)
    var forecast6Day: TextView = itemView.findViewById(R.id.forecast_6_day)
    var forecast6Min: TextView = itemView.findViewById(R.id.forecast_6_min)
    var forecast6Max: TextView = itemView.findViewById(R.id.forecast_6_max)
    var forecast6Icon: ImageView = itemView.findViewById(R.id.forecast_6_icon)
    var forecast7Day: TextView = itemView.findViewById(R.id.forecast_7_day)
    var forecast7Min: TextView = itemView.findViewById(R.id.forecast_7_min)
    var forecast7Max: TextView = itemView.findViewById(R.id.forecast_7_max)
    var forecast7Icon: ImageView = itemView.findViewById(R.id.forecast_7_icon)
}