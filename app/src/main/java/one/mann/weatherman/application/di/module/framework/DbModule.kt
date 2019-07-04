package one.mann.weatherman.application.di.module.framework

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import one.mann.weatherman.framework.data.database.WeatherDb
import javax.inject.Singleton

@Module
internal class DbModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): WeatherDb =
            Room.databaseBuilder(context, WeatherDb::class.java, "weather-db")
                    .build()
}