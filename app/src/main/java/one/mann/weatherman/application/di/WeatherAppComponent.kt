package one.mann.weatherman.application.di

import android.content.Context
import dagger.Component
import one.mann.weatherman.api.common.di.ApiServiceModule
import one.mann.weatherman.api.openweathermap.OwmService
import one.mann.weatherman.api.teleport.TeleportService
import one.mann.weatherman.framework.data.database.WeatherDb
import one.mann.weatherman.framework.data.database.di.DbModule
import javax.inject.Singleton

@Singleton
@Component(modules = [WeatherAppModule::class, ApiServiceModule::class, DbModule::class])
internal interface WeatherAppComponent {

    fun getContext(): Context

    fun getTeleportService(): TeleportService

    fun getOwmService(): OwmService

    fun getWeatherDb(): WeatherDb
}