package one.mann.weatherman.framework.data.database

import one.mann.domain.model.Location
import one.mann.domain.model.Weather
import one.mann.interactors.data.source.IDbDataSource

internal class DbDataSource(db: WeatherDb) : IDbDataSource {

    private val dao = db.weatherDao()

    override suspend fun insertWeather(weather: Weather) = dao.insert(weather.mapToDb())

    override suspend fun getDbSize(): Int = dao.tableSize()

    override suspend fun getAllWeather(): List<Weather> = dao.getAll().map { it.mapToDomain() }

    override suspend fun getAllLocations(): MutableList<Location> =
            dao.getAllLocations().map { it.mapToDomain() }.toMutableList()

    override suspend fun updateAllWeather(weathers: List<Weather>) =
            dao.updateAll(weathers.mapIndexed { i, it -> it.mapToDb(i + 1) })

    override suspend fun deleteWeather(name: String) = dao.delete(name)
}