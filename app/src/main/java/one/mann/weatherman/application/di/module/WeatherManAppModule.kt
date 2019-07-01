package one.mann.weatherman.application.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import one.mann.weatherman.application.WeatherManApp
import javax.inject.Inject

@Module
internal class WeatherManAppModule @Inject constructor(private val application: WeatherManApp) {

    @Provides
    fun provideApplicationContext(): Context = application
}