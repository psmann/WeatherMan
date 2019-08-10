package one.mann.weatherman.ui.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.logic.DEGREES
import one.mann.domain.model.Weather
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.util.inflateView
import one.mann.weatherman.ui.common.util.loadIcon
import one.mann.weatherman.ui.detail.adapter.WeatherViewHolder.*

internal class DetailRecyclerAdapter : RecyclerView.Adapter<WeatherViewHolder>() {

    companion object {
        private const val VIEW_HOLDER_COUNT = 5
    }

    private var weather = Weather()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder = when (viewType) {
        0 -> Current(parent.inflateView(R.layout.weather_current))
        1 -> Conditions(parent.inflateView(R.layout.weather_conditions))
        2 -> SunCycle(parent.inflateView(R.layout.weather_sun_cycle))
        3 -> HourlyForecast(parent.inflateView(R.layout.weather_forecast_hourly))
        else -> DailyForecast(parent.inflateView(R.layout.weather_forecast_daily))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) = when (holder) {
        is Current -> {
            holder.cityName.text = weather.cityName
            holder.lastChecked.text = weather.lastChecked
            holder.minTemp.text = weather.day1MinTemp
            holder.maxTemp.text = weather.day1MaxTemp
            holder.feelsLike.text = weather.feelsLike
            holder.currentTemp.text = weather.currentTemp
            holder.description.text = weather.description
            holder.weatherIcon.loadIcon(weather.iconId, weather.sunPosition)
        }
        is Conditions -> {
            holder.visibility.text = weather.visibility
            holder.pressure.text = weather.pressure
            holder.clouds.text = weather.clouds
            holder.windSpeed.text = weather.windSpeed
            holder.windDirection.text = weather.windDirection
            holder.windDirectionIcon.rotation = weather.windDirection.replace(DEGREES, "").toFloat()
            holder.location.text = weather.locationString
            holder.flagIcon.text = weather.countryFlag
            holder.lastUpdated.text = weather.lastUpdated
            holder.humidity.text = weather.humidity
        }
        is SunCycle -> {
            holder.setIsRecyclable(false) // Force-reload sunGraphView to fix view not updating issue
            holder.dayLength.text = weather.dayLength
            holder.sunrise.text = weather.sunrise
            holder.sunset.text = weather.sunset
            holder.sunGraphView.setT(weather.sunPosition)
        }
        is HourlyForecast -> {
            holder.forecast1Time.text = weather.hour03Time
            holder.forecast1Temp.text = weather.hour03Temp
            holder.forecast2Time.text = weather.hour06Time
            holder.forecast2Temp.text = weather.hour06Temp
            holder.forecast3Time.text = weather.hour09Time
            holder.forecast3Temp.text = weather.hour09Temp
            holder.forecast4Time.text = weather.hour12Time
            holder.forecast4Temp.text = weather.hour12Temp
            holder.forecast5Time.text = weather.hour15Time
            holder.forecast5Temp.text = weather.hour15Temp
            holder.forecast6Time.text = weather.hour18Time
            holder.forecast6Temp.text = weather.hour18Temp
            holder.forecast7Time.text = weather.hour21Time
            holder.forecast7Temp.text = weather.hour21Temp
            holder.forecast1Icon.loadIcon(weather.hour03IconId, weather.hour03SunPosition)
            holder.forecast2Icon.loadIcon(weather.hour06IconId, weather.hour06SunPosition)
            holder.forecast3Icon.loadIcon(weather.hour09IconId, weather.hour09SunPosition)
            holder.forecast4Icon.loadIcon(weather.hour12IconId, weather.hour12SunPosition)
            holder.forecast5Icon.loadIcon(weather.hour15IconId, weather.hour15SunPosition)
            holder.forecast6Icon.loadIcon(weather.hour18IconId, weather.hour18SunPosition)
            holder.forecast7Icon.loadIcon(weather.hour21IconId, weather.hour21SunPosition)
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
            holder.forecast1Icon.loadIcon(weather.day1IconId)
            holder.forecast2Icon.loadIcon(weather.day2IconId)
            holder.forecast3Icon.loadIcon(weather.day3IconId)
            holder.forecast4Icon.loadIcon(weather.day4IconId)
            holder.forecast5Icon.loadIcon(weather.day5IconId)
            holder.forecast6Icon.loadIcon(weather.day6IconId)
            holder.forecast7Icon.loadIcon(weather.day7IconId)
        }
    }

    override fun getItemCount(): Int = VIEW_HOLDER_COUNT

    override fun getItemViewType(position: Int): Int = position

    fun update(weather: Weather) {
        this.weather = weather
        notifyDataSetChanged()
    }
}