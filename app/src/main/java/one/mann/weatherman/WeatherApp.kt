package one.mann.weatherman

import android.app.Application
import androidx.room.Room
import one.mann.weatherman.framework.data.database.WeatherDb

internal class WeatherApp : Application() {

    lateinit var db: WeatherDb
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
                this,
                WeatherDb::class.java, "weather-db"
        ).build()
    }
}