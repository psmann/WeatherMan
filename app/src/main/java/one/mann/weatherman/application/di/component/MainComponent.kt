package one.mann.weatherman.application.di.component

import dagger.Subcomponent
import one.mann.weatherman.application.di.annotation.ActivityScope
import one.mann.weatherman.application.di.module.api.ApiServiceModule
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment
import one.mann.weatherman.ui.settings.SettingsActivity

@ActivityScope
@Subcomponent//(modules = [ApiServiceModule::class])
internal interface MainComponent {

    fun injectActivity(mainActivity: MainActivity)

    fun injectFragment(mainFragment: MainFragment)
}