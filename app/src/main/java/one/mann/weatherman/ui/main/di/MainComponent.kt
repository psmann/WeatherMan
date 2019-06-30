package one.mann.weatherman.ui.main.di

import dagger.Component
import one.mann.weatherman.api.openweathermap.di.OwmSourceModule
import one.mann.weatherman.api.teleport.di.TeleportSourceModule
import one.mann.weatherman.application.di.WeatherManAppComponent
import one.mann.weatherman.framework.data.database.di.DbSourceModule
import one.mann.weatherman.framework.data.location.di.LocationSourceModule
import one.mann.weatherman.ui.common.di.ActivityScope
import one.mann.weatherman.ui.common.di.ViewModelModule
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment

@ActivityScope
@Component(dependencies = [WeatherManAppComponent::class],
        modules = [OwmSourceModule::class, TeleportSourceModule::class, DbSourceModule::class,
            LocationSourceModule::class, ViewModelModule::class])
internal interface MainComponent {

    fun injectActivity(mainActivity: MainActivity)

    fun injectFragment(mainFragment: MainFragment)
}