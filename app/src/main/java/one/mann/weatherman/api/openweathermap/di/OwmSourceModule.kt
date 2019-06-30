package one.mann.weatherman.api.openweathermap.di

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.source.IApiWeatherSource
import one.mann.weatherman.api.openweathermap.OwmDataSource

@Module
internal abstract class OwmSourceModule {

    @Binds
    abstract fun bindOwmDataSource(owmDataSource: OwmDataSource): IApiWeatherSource
}