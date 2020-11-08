package one.mann.weatherman.di.components

import dagger.Subcomponent
import one.mann.weatherman.di.annotations.scope.ActivityScope
import one.mann.weatherman.ui.detail.DetailActivity
import one.mann.weatherman.ui.main.MainActivity
import one.mann.weatherman.ui.main.CityFragment

/* Created by Psmann. */

@ActivityScope
@Subcomponent
internal interface WeatherSubComponent {

    fun injectMainActivity(mainActivity: MainActivity)

    fun injectMainFragment(cityFragment: CityFragment)

    fun injectDetailActivity(detailActivity: DetailActivity)
}