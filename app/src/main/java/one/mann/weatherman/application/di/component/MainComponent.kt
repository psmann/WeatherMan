package one.mann.weatherman.application.di.component

import dagger.Subcomponent
import one.mann.weatherman.application.di.annotation.ActivityScope
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment

@ActivityScope
@Subcomponent
internal interface MainComponent {

    fun injectActivity(mainActivity: MainActivity)

    fun injectFragment(mainFragment: MainFragment)
}