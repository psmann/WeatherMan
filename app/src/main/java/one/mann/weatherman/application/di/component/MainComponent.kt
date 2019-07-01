package one.mann.weatherman.application.di.component

import dagger.Component
import one.mann.weatherman.application.di.module.api.OwmSourceModule
import one.mann.weatherman.application.di.module.api.TeleportSourceModule
import one.mann.weatherman.application.di.module.framework.DbSourceModule
import one.mann.weatherman.application.di.module.framework.LocationSourceModule
import one.mann.weatherman.application.di.annotation.ActivityScope
import one.mann.weatherman.application.di.module.ui.ViewModelModule
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