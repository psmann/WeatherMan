package one.mann.weatherman.framework.data.database

import one.mann.domain.models.NotificationData
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.weatherman.framework.data.database.entities.CurrentWeather
import one.mann.weatherman.framework.data.database.entities.DailyForecast
import one.mann.weatherman.framework.data.database.entities.HourlyForecast
import javax.inject.Inject
import one.mann.domain.models.weather.Weather as DomainWeather

/* Created by Psmann. */

internal class WeatherDbDataSource @Inject constructor(db: WeatherDb) : DatabaseDataSource {

    private val dao = db.weatherDao()

    /** Insert a new City along with its weather information (i.e. CurrentWeather, DailyForecasts and HourlyForecasts) */
    override suspend fun insertCityAndWeather(weather: DomainWeather) {
        dao.insertCity(weather.mapToDbCity())
        dao.insertCurrentWeather(weather.mapToDbCurrentWeather())
        dao.insertDailyForecasts(weather.mapToDbDailyForecasts())
        dao.insertHourlyForecasts(weather.mapToDbHourlyForecasts())
    }

    /** Get all the notification data from the database */
    override suspend fun getNotificationData(): NotificationData {
        val userCity = dao.getCityNameForUserLocation()
        val todayForecast = dao.getTodayForecastForUserLocation(userCity.cityId)
        val currentWeatherWithHourlyForecasts = dao.getHourlyForecastsForUserLocation(userCity.cityId)

        return currentWeatherWithHourlyForecasts.mapToDomain(userCity.cityName, todayForecast)
    }

    /** Get all Cities, CurrentWeathers, DailyForecasts and HourlyForecasts and pass them as Domain Weather objects */
    override suspend fun getAllCitiesAndWeathers(): List<DomainWeather> {
        val cities = dao.getAllCities()
        val weathers = mutableListOf<DomainWeather>()
        cities.forEach {
            val currentWeather = dao.getCurrentWeather(it.cityId).currentWeather
            val dailyForecasts = dao.getCDailyForecasts(it.cityId).getSortedForecast()
            val hourlyForecasts = dao.getHourlyForecasts(it.cityId).getSortedForecast()
            weathers.add(it.mapToDomainWeather(currentWeather, dailyForecasts, hourlyForecasts))
        }

        return weathers
    }

    /** Update the lastChecked value in CurrentWeather entity for all the rows */
    override suspend fun updateLastChecked(lastChecked: Long) {
        val updatedCurrentWeathers = dao.getAllCurrentWeather().map { it.copy(lastChecked = lastChecked) }
        dao.updateCurrentWeathers(updatedCurrentWeathers)
    }

    /** Update All CurrentWeathers, DailyForecasts and HourlyForecasts */
    override suspend fun updateAllWeathers(weathers: List<DomainWeather>) {
        val currentWeathersDb = mutableListOf<CurrentWeather>()
        val dailyForecastsDb = mutableListOf<DailyForecast>()
        val hourlyForecastsDb = mutableListOf<HourlyForecast>()

        weathers.forEachIndexed { i, weather ->
            // Only update user city in the database as only it can change (i.e. if user location changes)
            if (i == 0) dao.updateCity(weather.mapToDbCity())
            currentWeathersDb.add(weather.mapToDbCurrentWeather())
            dailyForecastsDb.addAll(weather.mapToDbDailyForecasts())
            hourlyForecastsDb.addAll(weather.mapToDbHourlyForecasts())
        }
        dao.updateCurrentWeathers(currentWeathersDb)
        dao.updateDailyForecasts(dailyForecastsDb)
        dao.updateHourlyForecasts(hourlyForecastsDb)
    }

    /** Delete a City as well as all the weather information associated with it */
    override suspend fun deleteCityAndWeather(cityId: String) {
        dao.deleteCity(cityId)
        dao.deleteCurrentWeather(cityId)
        dao.deleteDailyForecasts(cityId)
        dao.deleteHourlyForecasts(cityId)
    }
}