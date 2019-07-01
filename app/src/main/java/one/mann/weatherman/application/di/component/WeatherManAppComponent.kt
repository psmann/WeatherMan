package one.mann.weatherman.application.di.component

import android.content.Context
import dagger.Component
import one.mann.weatherman.application.di.module.api.ApiServiceModule
import one.mann.weatherman.api.openweathermap.OwmService
import one.mann.weatherman.api.teleport.TeleportService
import one.mann.weatherman.application.di.module.WeatherManAppModule
import one.mann.weatherman.framework.data.database.WeatherDb
import one.mann.weatherman.application.di.module.framework.DbModule
import javax.inject.Singleton

@Singleton
@Component(modules = [WeatherManAppModule::class, ApiServiceModule::class, DbModule::class])

internal interface WeatherManAppComponent {

    fun getContext(): Context

    fun getTeleportService(): TeleportService

    fun getOwmService(): OwmService

    fun getWeatherDb(): WeatherDb
}