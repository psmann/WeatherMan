package one.mann.weatherman.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import one.mann.weatherman.application.WeatherApp
import javax.inject.Inject

@Module
internal class WeatherAppModule @Inject constructor(private val application: WeatherApp) {

    @Provides
    fun provideApplicationContext(): Context = application
}