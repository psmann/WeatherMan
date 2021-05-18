package one.mann.weatherman.framework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import one.mann.weatherman.framework.data.database.entities.City
import one.mann.weatherman.framework.data.database.entities.CurrentWeather
import one.mann.weatherman.framework.data.database.entities.DailyForecast
import one.mann.weatherman.framework.data.database.entities.HourlyForecast

/* Created by Psmann. */

@Database(
    entities = [
        City::class,
        CurrentWeather::class,
        DailyForecast::class,
        HourlyForecast::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class WeatherDb : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}