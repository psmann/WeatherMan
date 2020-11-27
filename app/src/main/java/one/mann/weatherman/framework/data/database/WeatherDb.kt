package one.mann.weatherman.framework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import one.mann.weatherman.framework.data.database.model.Weather

/* Created by Psmann. */

@Database(entities = [Weather::class], version = 1, exportSchema = false)
internal abstract class WeatherDb : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}