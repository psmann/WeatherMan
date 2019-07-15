package one.mann.weatherman.application.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import one.mann.weatherman.application.WeatherManApp
import javax.inject.Inject
import javax.inject.Singleton

@Module
internal class WeatherManAppModule @Inject constructor(private val application: WeatherManApp) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideDefaultPreferences(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
}