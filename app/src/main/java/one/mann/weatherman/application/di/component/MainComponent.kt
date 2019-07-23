package one.mann.weatherman.application.di.component

import dagger.Subcomponent
import one.mann.weatherman.application.di.annotation.ActivityScope
import one.mann.weatherman.application.di.module.framework.WorkerModule
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment

@ActivityScope
@Subcomponent(modules = [WorkerModule::class])
internal interface MainComponent {

    fun injectActivity(mainActivity: MainActivity)

    fun injectFragment(mainFragment: MainFragment)
}