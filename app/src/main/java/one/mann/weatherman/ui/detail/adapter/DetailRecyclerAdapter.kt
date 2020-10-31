package one.mann.weatherman.ui.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.logic.DEGREES
import one.mann.domain.logic.removeUnits
import one.mann.domain.model.weather.Weather
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
        0 -> Current(parent.inflateView(R.layout.item_weather_current))
        1 -> Conditions(parent.inflateView(R.layout.item_weather_conditions))
        2 -> SunCycle(parent.inflateView(R.layout.item_weather_sun_cycle))
        3 -> HourlyForecast(parent.inflateView(R.layout.item_weather_forecast_hourly))
        else -> DailyForecast(parent.inflateView(R.layout.item_weather_forecast_daily))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        when (holder) {
            is Current -> holder.binding.apply {
                currentCityNameTextView.text = weather.cityName
                currentLastCheckedResultTextView.text = weather.lastChecked
                currentMinTempResultTextView.text = weather.day1MinTemp
                currentMaxTempResultTextView.text = weather.day1MaxTemp
                currentFeelsLikeResultTextView.text = weather.feelsLike
                currentCurrentTempResultTextView.text = weather.currentTemp
                currentDescriptionTextView.text = weather.description
                currentWeatherIconImageView.loadIcon(weather.iconId, weather.sunPosition)
            }
            is Conditions -> holder.binding.apply {
                conditionsVisibilityResultTextView.text = weather.visibility
                conditionsPressureResultTextView.text = weather.pressure
                conditionsCloudsResultTextView.text = weather.clouds
                conditionsWindSpeedResultTextView.text = weather.windSpeed
                conditionsWindDirectionResultTextView.text = weather.windDirection
                conditionsWindDirIconImageView.rotation = weather.windDirection.removeUnits(DEGREES).toFloat() // Rotate icon
                conditionsLocationResultTextView.text = weather.locationString
                conditionsCountryFlagTextView.text = weather.countryFlag
                conditionsLastUpdatedResultTextView.text = weather.lastUpdated
                conditionsHumidityResultTextView.text = weather.humidity
            }
            is SunCycle -> holder.binding.apply {
                holder.setIsRecyclable(false) // This force reloads sunPositionView to fix view not updating issue
                sunCycleDayLengthResultTextView.text = weather.dayLength
                sunCycleSunriseResultTextView.text = weather.sunrise
                sunCycleSunsetResultTextView.text = weather.sunset
                sunCycleSunPositionView.setT(weather.sunPosition)
            }
            is HourlyForecast -> holder.binding.apply {
                val forecastList = listOf(
                        weather.hour03Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour06Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour09Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour12Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour15Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour18Temp.removeUnits(DEGREES).toFloat(),
                        weather.hour21Temp.removeUnits(DEGREES).toFloat()
                )
                holder.setIsRecyclable(false) // This force reloads ForecastView to fix view not updating issue
                forecastHourly1TimeTextView.text = weather.hour03Time
                forecastHourly1TempTextView.text = weather.hour03Temp
                forecastHourly2TimeTextView.text = weather.hour06Time
                forecastHourly2TempTextView.text = weather.hour06Temp
                forecastHourly3TimeTextView.text = weather.hour09Time
                forecastHourly3TempTextView.text = weather.hour09Temp
                forecastHourly4TimeTextView.text = weather.hour12Time
                forecastHourly4TempTextView.text = weather.hour12Temp
                forecastHourly5TimeTextView.text = weather.hour15Time
                forecastHourly5TempTextView.text = weather.hour15Temp
                forecastHourly6TimeTextView.text = weather.hour18Time
                forecastHourly6TempTextView.text = weather.hour18Temp
                forecastHourly7TimeTextView.text = weather.hour21Time
                forecastHourly7TempTextView.text = weather.hour21Temp
                forecastHourly1IconImageView.loadIcon(weather.hour03IconId, weather.hour03SunPosition)
                forecastHourly2IconImageView.loadIcon(weather.hour06IconId, weather.hour06SunPosition)
                forecastHourly3IconImageView.loadIcon(weather.hour09IconId, weather.hour09SunPosition)
                forecastHourly4IconImageView.loadIcon(weather.hour12IconId, weather.hour12SunPosition)
                forecastHourly5IconImageView.loadIcon(weather.hour15IconId, weather.hour15SunPosition)
                forecastHourly6IconImageView.loadIcon(weather.hour18IconId, weather.hour18SunPosition)
                forecastHourly7IconImageView.loadIcon(weather.hour21IconId, weather.hour21SunPosition)
                forecastHourlyForecastGraphView.setPoints(forecastList) // Set points for forecast graph
            }
            is DailyForecast -> holder.binding.apply {
                forecastDaily1DayTextView.text = weather.day1Date
                forecastDaily1MinTextView.text = weather.day1MinTemp
                forecastDaily1MaxTextView.text = weather.day1MaxTemp
                forecastDaily2DayTextView.text = weather.day2Date
                forecastDaily2MinTextView.text = weather.day2MinTemp
                forecastDaily2MaxTextView.text = weather.day2MaxTemp
                forecastDaily3DayTextView.text = weather.day3Date
                forecastDaily3MinTextView.text = weather.day3MinTemp
                forecastDaily3MaxTextView.text = weather.day3MaxTemp
                forecastDaily4DayTextView.text = weather.day4Date
                forecastDaily4MinTextView.text = weather.day4MinTemp
                forecastDaily4MaxTextView.text = weather.day4MaxTemp
                forecastDaily5DayTextView.text = weather.day5Date
                forecastDaily5MinTextView.text = weather.day5MinTemp
                forecastDaily5MaxTextView.text = weather.day5MaxTemp
                forecastDaily6DayTextView.text = weather.day6Date
                forecastDaily6MinTextView.text = weather.day6MinTemp
                forecastDaily6MaxTextView.text = weather.day6MaxTemp
                forecastDaily7DayTextView.text = weather.day7Date
                forecastDaily7MinTextView.text = weather.day7MinTemp
                forecastDaily7MaxTextView.text = weather.day7MaxTemp
                forecastDaily1IconImageView.loadIcon(weather.day1IconId)
                forecastDaily2IconImageView.loadIcon(weather.day2IconId)
                forecastDaily3IconImageView.loadIcon(weather.day3IconId)
                forecastDaily4IconImageView.loadIcon(weather.day4IconId)
                forecastDaily5IconImageView.loadIcon(weather.day5IconId)
                forecastDaily6IconImageView.loadIcon(weather.day6IconId)
                forecastDaily7IconImageView.loadIcon(weather.day7IconId)
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