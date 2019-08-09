package one.mann.weatherman.di.component

import dagger.Component
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.di.module.WeatherManAppModule
import one.mann.weatherman.di.module.api.ApiDataSourceModule
import one.mann.weatherman.di.module.api.ApiServiceModule
import one.mann.weatherman.di.module.framework.DbModule
import one.mann.weatherman.di.module.framework.FrameworkDataSourceModule
import one.mann.weatherman.di.module.framework.LocationModule
import one.mann.weatherman.di.module.framework.WorkerModule
import one.mann.weatherman.di.module.ui.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    WeatherManAppModule::class,
    ApiServiceModule::class,
    LocationModule::class,
    DbModule::class,
    WorkerModule::class,
    ViewModelModule::class,
    ApiDataSourceModule::class,
    FrameworkDataSourceModule::class
])
internal interface WeatherManAppComponent {

    fun getSubComponent(): WeatherSubComponent

    fun injectApplication(weatherManApp: WeatherManApp)
}