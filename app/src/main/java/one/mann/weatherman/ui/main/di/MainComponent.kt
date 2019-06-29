package one.mann.weatherman.ui.main.di

import dagger.Component
import one.mann.weatherman.api.common.di.ApiModule
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment

@Component(modules = [ApiModule::class])
internal interface MainComponent {

    fun injectActivity(mainActivity: MainActivity)

    fun injectFragment(mainFragment: MainFragment)
}