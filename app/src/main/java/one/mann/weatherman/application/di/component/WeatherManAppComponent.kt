package one.mann.weatherman.application.di.component

import dagger.Component
import one.mann.weatherman.application.di.module.WeatherManAppModule
import one.mann.weatherman.application.di.module.api.ApiDataSourceModule
import one.mann.weatherman.application.di.module.api.ApiServiceModule
import one.mann.weatherman.application.di.module.framework.DbModule
import one.mann.weatherman.application.di.module.framework.FrameworkDataSourceModule
import one.mann.weatherman.application.di.module.framework.LocationModule
import one.mann.weatherman.application.di.module.ui.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [WeatherManAppModule::class, ApiServiceModule::class, LocationModule::class,
    DbModule::class, ViewModelModule::class, ApiDataSourceModule::class, FrameworkDataSourceModule::class])
internal interface WeatherManAppComponent {

    fun getMainComponent(): MainComponent
}