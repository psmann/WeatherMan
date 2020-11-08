package one.mann.weatherman.di.modules.framework

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import one.mann.weatherman.framework.data.database.WeatherDb
import javax.inject.Singleton

/* Created by Psmann. */

@Module
internal class DbModule {

    companion object {
        private const val DB_NAME = "weather-db"
    }

    @Provides
    @Singleton
    fun provideDb(context: Context): WeatherDb = Room.databaseBuilder(context, WeatherDb::class.java, DB_NAME).build()
}