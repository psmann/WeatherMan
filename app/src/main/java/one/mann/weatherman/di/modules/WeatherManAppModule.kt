package one.mann.weatherman.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import one.mann.weatherman.WeatherManApp
import javax.inject.Inject
import javax.inject.Singleton

@Module
internal class WeatherManAppModule @Inject constructor(private val application: WeatherManApp) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideDefaultPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)
}