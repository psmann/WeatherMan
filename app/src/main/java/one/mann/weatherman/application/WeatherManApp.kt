package one.mann.weatherman.application

import android.app.Application
import one.mann.weatherman.application.di.DaggerWeatherManAppComponent
import one.mann.weatherman.application.di.WeatherManAppComponent
import one.mann.weatherman.application.di.WeatherManAppModule

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