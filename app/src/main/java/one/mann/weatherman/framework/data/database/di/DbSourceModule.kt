package one.mann.weatherman.framework.data.database.di

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.source.IDbDataSource
import one.mann.weatherman.framework.data.database.DbDataSource

@Module
internal abstract class DbSourceModule {

    @Binds
    abstract fun bindDbDataSource(dbDataSource: DbDataSource): IDbDataSource
}