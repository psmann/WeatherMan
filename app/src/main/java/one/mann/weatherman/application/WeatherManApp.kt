package one.mann.weatherman.application

import android.app.Application
import one.mann.weatherman.application.di.component.DaggerWeatherManAppComponent
import one.mann.weatherman.application.di.component.WeatherManAppComponent
import one.mann.weatherman.application.di.module.WeatherManAppModule

internal class WeatherManApp : Application() {

    companion object {
        lateinit var appComponent: WeatherManAppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerWeatherManAppComponent.builder()
                .weatherManAppModule(WeatherManAppModule(this@WeatherManApp))
                .build()
    }
}