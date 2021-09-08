package one.mann.weatherman

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import one.mann.weatherman.di.components.DaggerWeatherManAppComponent
import one.mann.weatherman.di.components.WeatherManAppComponent
import one.mann.weatherman.di.modules.WeatherManAppModule
import javax.inject.Inject

/* Created by Psmann. */

internal class WeatherManApp : Application() {

    companion object {
        lateinit var appComponent: WeatherManAppComponent
            private set
    }

    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        // Dagger setup
        appComponent = DaggerWeatherManAppComponent.builder()
            .weatherManAppModule(WeatherManAppModule(this))
            .build()
            .apply { injectApplication(this@WeatherManApp) }
        // WorkManager setup
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }
}