package one.mann.weatherman.application.di.module.framework

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.source.IDeviceLocationSource
import one.mann.weatherman.framework.data.location.LocationDataSource

@Module
internal abstract class LocationSourceModule {

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: LocationDataSource): IDeviceLocationSource
}