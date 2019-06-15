package one.mann.weatherman.framework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Weather::class], version = 1)
abstract class WeatherDb: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}