package one.mann.weatherman.di.components

import dagger.Component
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.di.modules.WeatherManAppModule
import one.mann.weatherman.di.modules.api.ApiDataSourceModule
import one.mann.weatherman.di.modules.api.ApiServiceModule
import one.mann.weatherman.di.modules.framework.DbModule
import one.mann.weatherman.di.modules.framework.FrameworkDataSourceModule
import one.mann.weatherman.di.modules.framework.LocationModule
import one.mann.weatherman.di.modules.framework.WorkerModule
import one.mann.weatherman.di.modules.ui.ViewModelModule
import javax.inject.Singleton

/* Created by Psmann. */

@Singleton
@Component(
    modules = [
        WeatherManAppModule::class,
        ApiServiceModule::class,
        LocationModule::class,
        DbModule::class,
        WorkerModule::class,
        ViewModelModule::class,
        ApiDataSourceModule::class,
        FrameworkDataSourceModule::class
    ]
)
internal interface WeatherManAppComponent {

    fun getSubComponent(): WeatherSubComponent

    fun injectApplication(weatherManApp: WeatherManApp)
}