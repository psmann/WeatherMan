package one.mann.weatherman.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.model.Weather
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.util.inflateView
import one.mann.weatherman.ui.common.util.loadImage
import one.mann.weatherman.ui.main.adapter.WeatherViewHolder.*

internal class MainRecyclerAdapter : RecyclerView.Adapter<WeatherViewHolder>() {

    companion object {
        private const val VIEW_HOLDER_COUNT = 5
    }

    private var weather = Weather()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder = when (viewType) {
        0 -> City(parent.inflateView(R.layout.cardview_city))
        1 -> Main(parent.inflateView(R.layout.cardview_main))
        2 -> Sun(parent.inflateView(R.layout.cardview_sun))
        3 -> Clouds(parent.inflateView(R.layout.cardview_clouds))
        else -> DailyForecast(parent.inflateView(R.layout.cardview_forecast))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        when (holder) {
            is City -> {
                holder.cityName.text = weather.cityName
                holder.lastChecked.text = weather.lastChecked
                holder.location.text = weather.locationString
                holder.flagIcon.text = weather.countryFlag
            }
            is Main -> {
                holder.lastUpdated.text = weather.lastUpdated
                holder.humidity.text = weather.humidity
                holder.minTemp.text = weather.minTemp
                holder.maxTemp.text = weather.maxTemp
                holder.feelsLike.text = weather.feelsLike
                holder.currentTemp.text = weather.currentTemp
                holder.description.text = weather.description
                holder.weatherIcon.loadImage(weather.icon)
            }
            is Sun -> {
                holder.setIsRecyclable(false) // Force reload sunGraphView and fix not updating issue
                holder.dayLength.text = weather.dayLength
                holder.sunrise.text = weather.sunrise
                holder.sunset.text = weather.sunset
                holder.sunGraphView.setT(weather.sunPosition)
            }
            is Clouds -> {
                holder.visibility.text = weather.visibility
                holder.pressure.text = weather.pressure
                holder.clouds.text = weather.clouds
                holder.windSpeed.text = weather.windSpeed
                holder.windDirection.text = weather.windDirection
            }
            is DailyForecast -> {
                holder.forecast1Day.text = weather.day1Date
                holder.forecast1Min.text = weather.day1MinTemp
                holder.forecast1Max.text = weather.day1MaxTemp
                holder.forecast2Day.text = weather.day2Date
                holder.forecast2Min.text = weather.day2MinTemp
                holder.forecast2Max.text = weather.day2MaxTemp
                holder.forecast3Day.text = weather.day3Date
                holder.forecast3Min.text = weather.day3MinTemp
                holder.forecast3Max.text = weather.day3MaxTemp
                holder.forecast4Day.text = weather.day4Date
                holder.forecast4Min.text = weather.day4MinTemp
                holder.forecast4Max.text = weather.day4MaxTemp
                holder.forecast5Day.text = weather.day5Date
                holder.forecast5Min.text = weather.day5MinTemp
                holder.forecast5Max.text = weather.day5MaxTemp
                holder.forecast6Day.text = weather.day6Date
                holder.forecast6Min.text = weather.day6MinTemp
                holder.forecast6Max.text = weather.day6MaxTemp
                holder.forecast7Day.text = weather.day7Date
                holder.forecast7Min.text = weather.day7MinTemp
                holder.forecast7Max.text = weather.day7MaxTemp
                holder.forecast1Icon.loadImage(weather.day1Icon)
                holder.forecast2Icon.loadImage(weather.day2Icon)
                holder.forecast3Icon.loadImage(weather.day3Icon)
                holder.forecast4Icon.loadImage(weather.day4Icon)
                holder.forecast5Icon.loadImage(weather.day5Icon)
                holder.forecast6Icon.loadImage(weather.day6Icon)
                holder.forecast7Icon.loadImage(weather.day7Icon)
            }
        }
    }

    override fun getItemCount(): Int = VIEW_HOLDER_COUNT

    override fun getItemViewType(position: Int): Int = position

    fun update(weather: Weather) {
        this.weather = weather
        notifyDataSetChanged()
    }
}