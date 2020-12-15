package one.mann.weatherman.ui.detail.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import one.mann.domain.logic.DEGREES
import one.mann.domain.logic.removeUnits
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.models.Weather
import one.mann.weatherman.ui.common.util.inflateView
import one.mann.weatherman.ui.common.util.loadIcon
import one.mann.weatherman.ui.detail.adapters.WeatherViewHolder.*

/* Created by Psmann. */

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
                currentCityNameTextView.text = weather.city.cityName
                currentLastCheckedResultTextView.text = weather.currentWeather.lastChecked
                currentMinTempResultTextView.text = weather.dailyForecasts[0].minTemp
                currentMaxTempResultTextView.text = weather.dailyForecasts[0].maxTemp
                currentFeelsLikeResultTextView.text = weather.currentWeather.feelsLike
                currentCurrentTempResultTextView.text = weather.currentWeather.currentTemperature
                currentDescriptionTextView.text = weather.currentWeather.description
                currentWeatherIconImageView.loadIcon(weather.currentWeather.iconId, weather.currentWeather.sunPosition)
            }
            is Conditions -> holder.binding.apply {
                conditionsVisibilityResultTextView.text = weather.currentWeather.visibility
                conditionsPressureResultTextView.text = weather.currentWeather.pressure
                conditionsCloudsResultTextView.text = weather.currentWeather.clouds
                conditionsWindSpeedResultTextView.text = weather.currentWeather.windSpeed
                conditionsWindDirectionResultTextView.text = weather.currentWeather.windDirection
                conditionsWindDirIconImageView.rotation = weather.currentWeather.windDirection.removeUnits(DEGREES).toFloat() // Rotate icon
                conditionsLocationResultTextView.text = weather.city.coordinates
                conditionsCountryFlagTextView.text = weather.currentWeather.countryFlag
                conditionsLastUpdatedResultTextView.text = weather.currentWeather.lastUpdated
                conditionsHumidityResultTextView.text = weather.currentWeather.humidity
            }
            is SunCycle -> holder.binding.apply {
                holder.setIsRecyclable(false) // This force reloads sunPositionView to fix view not updating issue
                sunCycleDayLengthResultTextView.text = weather.currentWeather.dayLength
                sunCycleSunriseResultTextView.text = weather.currentWeather.sunrise
                sunCycleSunsetResultTextView.text = weather.currentWeather.sunset
                sunCycleSunPositionView.setT(weather.currentWeather.sunPosition)
            }
            is HourlyForecast -> holder.binding.apply {
                val forecastList = listOf(
                        weather.hourlyForecasts[0].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[1].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[2].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[3].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[4].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[5].temperature.removeUnits(DEGREES).toFloat(),
                        weather.hourlyForecasts[6].temperature.removeUnits(DEGREES).toFloat()
                )
                holder.setIsRecyclable(false) // This force reloads ForecastView to fix view not updating issue
                forecastHourly1TimeTextView.text = weather.hourlyForecasts[0].forecastTime
                forecastHourly1TempTextView.text = weather.hourlyForecasts[0].temperature
                forecastHourly2TimeTextView.text = weather.hourlyForecasts[1].forecastTime
                forecastHourly2TempTextView.text = weather.hourlyForecasts[1].temperature
                forecastHourly3TimeTextView.text = weather.hourlyForecasts[2].forecastTime
                forecastHourly3TempTextView.text = weather.hourlyForecasts[2].temperature
                forecastHourly4TimeTextView.text = weather.hourlyForecasts[3].forecastTime
                forecastHourly4TempTextView.text = weather.hourlyForecasts[3].temperature
                forecastHourly5TimeTextView.text = weather.hourlyForecasts[4].forecastTime
                forecastHourly5TempTextView.text = weather.hourlyForecasts[4].temperature
                forecastHourly6TimeTextView.text = weather.hourlyForecasts[5].forecastTime
                forecastHourly6TempTextView.text = weather.hourlyForecasts[5].temperature
                forecastHourly7TimeTextView.text = weather.hourlyForecasts[6].forecastTime
                forecastHourly7TempTextView.text = weather.hourlyForecasts[6].temperature
                forecastHourly1IconImageView.loadIcon(weather.hourlyForecasts[0].forecastIconId, weather.hourlyForecasts[0].sunPosition)
                forecastHourly2IconImageView.loadIcon(weather.hourlyForecasts[1].forecastIconId, weather.hourlyForecasts[1].sunPosition)
                forecastHourly3IconImageView.loadIcon(weather.hourlyForecasts[2].forecastIconId, weather.hourlyForecasts[2].sunPosition)
                forecastHourly4IconImageView.loadIcon(weather.hourlyForecasts[3].forecastIconId, weather.hourlyForecasts[3].sunPosition)
                forecastHourly5IconImageView.loadIcon(weather.hourlyForecasts[4].forecastIconId, weather.hourlyForecasts[4].sunPosition)
                forecastHourly6IconImageView.loadIcon(weather.hourlyForecasts[5].forecastIconId, weather.hourlyForecasts[5].sunPosition)
                forecastHourly7IconImageView.loadIcon(weather.hourlyForecasts[6].forecastIconId, weather.hourlyForecasts[6].sunPosition)
                forecastHourlyForecastGraphView.setPoints(forecastList) // Set points for forecast graph
            }
            is DailyForecast -> holder.binding.apply {
                forecastDaily1DayTextView.text = weather.dailyForecasts[0].forecastDate
                forecastDaily1MinTextView.text = weather.dailyForecasts[0].minTemp
                forecastDaily1MaxTextView.text = weather.dailyForecasts[0].maxTemp
                forecastDaily2DayTextView.text = weather.dailyForecasts[1].forecastDate
                forecastDaily2MinTextView.text = weather.dailyForecasts[1].minTemp
                forecastDaily2MaxTextView.text = weather.dailyForecasts[1].maxTemp
                forecastDaily3DayTextView.text = weather.dailyForecasts[2].forecastDate
                forecastDaily3MinTextView.text = weather.dailyForecasts[2].minTemp
                forecastDaily3MaxTextView.text = weather.dailyForecasts[2].maxTemp
                forecastDaily4DayTextView.text = weather.dailyForecasts[3].forecastDate
                forecastDaily4MinTextView.text = weather.dailyForecasts[3].minTemp
                forecastDaily4MaxTextView.text = weather.dailyForecasts[3].maxTemp
                forecastDaily5DayTextView.text = weather.dailyForecasts[4].forecastDate
                forecastDaily5MinTextView.text = weather.dailyForecasts[4].minTemp
                forecastDaily5MaxTextView.text = weather.dailyForecasts[4].maxTemp
                forecastDaily6DayTextView.text = weather.dailyForecasts[5].forecastDate
                forecastDaily6MinTextView.text = weather.dailyForecasts[5].minTemp
                forecastDaily6MaxTextView.text = weather.dailyForecasts[5].maxTemp
                forecastDaily7DayTextView.text = weather.dailyForecasts[6].forecastDate
                forecastDaily7MinTextView.text = weather.dailyForecasts[6].minTemp
                forecastDaily7MaxTextView.text = weather.dailyForecasts[6].maxTemp
                forecastDaily1IconImageView.loadIcon(weather.dailyForecasts[0].forecastIconId)
                forecastDaily2IconImageView.loadIcon(weather.dailyForecasts[1].forecastIconId)
                forecastDaily3IconImageView.loadIcon(weather.dailyForecasts[2].forecastIconId)
                forecastDaily4IconImageView.loadIcon(weather.dailyForecasts[3].forecastIconId)
                forecastDaily5IconImageView.loadIcon(weather.dailyForecasts[4].forecastIconId)
                forecastDaily6IconImageView.loadIcon(weather.dailyForecasts[5].forecastIconId)
                forecastDaily7IconImageView.loadIcon(weather.dailyForecasts[6].forecastIconId)
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