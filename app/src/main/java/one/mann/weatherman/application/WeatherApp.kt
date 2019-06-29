package one.mann.weatherman.application

import android.app.Application
import one.mann.weatherman.application.di.DaggerWeatherAppComponent
import one.mann.weatherman.application.di.WeatherAppComponent
import one.mann.weatherman.application.di.WeatherAppModule

internal class WeatherApp : Application() {

    companion object {
        lateinit var component: WeatherAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerWeatherAppComponent.builder()
                .weatherAppModule(WeatherAppModule(this@WeatherApp))
                .build()
    }
}