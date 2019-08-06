package one.mann.weatherman.application.di.component

import dagger.Subcomponent
import one.mann.weatherman.application.di.annotation.ActivityScope
import one.mann.weatherman.ui.detail.DetailActivity
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.MainFragment

@ActivityScope
@Subcomponent
internal interface WeatherSubComponent {

    fun injectMainActivity(mainActivity: MainActivity)

    fun injectMainFragment(mainFragment: MainFragment)

    fun injectDetailActivity(detailActivity: DetailActivity)
}