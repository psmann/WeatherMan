package one.mann.weatherman.framework.data.database

import one.mann.domain.model.Location
import one.mann.domain.model.Weather
import one.mann.interactors.data.source.IDbDataSource

internal class DbDataSource: IDbDataSource {
    override suspend fun getAllLocations(): MutableList<Location> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isDbEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertWeather(weather: Weather) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllWeather(): List<Weather> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateAllWeather(weathers: List<Weather>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteWeather(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}