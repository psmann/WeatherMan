package one.mann.weatherman.api.teleport.di

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.weatherman.api.teleport.TeleportDataSource

@Module
internal abstract class TeleportSourceModule {

    @Binds
    abstract fun bindTeleportDataSource(teleportDataSource: TeleportDataSource): IApiTimezoneSource
}