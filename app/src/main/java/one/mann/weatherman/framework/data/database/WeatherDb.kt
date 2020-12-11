package one.mann.weatherman.framework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import one.mann.weatherman.framework.data.database.entities.CurrentWeather

/* Created by Psmann. */

@Database(entities = [CurrentWeather::class], version = 1, exportSchema = false)
internal abstract class WeatherDb : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}