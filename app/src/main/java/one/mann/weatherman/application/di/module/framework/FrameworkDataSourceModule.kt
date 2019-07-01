package one.mann.weatherman.application.di.module.framework

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.source.IDbDataSource
import one.mann.interactors.data.source.IDeviceLocationSource
import one.mann.weatherman.framework.data.database.DbDataSource
import one.mann.weatherman.framework.data.location.LocationDataSource

@Module
internal abstract class FrameworkDataSourceModule {

    @Binds
    abstract fun bindDbDataSource(dbDataSource: DbDataSource): IDbDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: LocationDataSource): IDeviceLocationSource
}