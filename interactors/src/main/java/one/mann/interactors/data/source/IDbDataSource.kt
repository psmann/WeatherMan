package one.mann.interactors.data.source

import one.mann.domain.model.Location
import one.mann.domain.model.Weather

interface IDbDataSource {

    suspend fun isDbEmpty(): Boolean

    suspend fun insertWeather(weather: Weather)

    suspend fun getAllWeather(): List<Weather>

    suspend fun updateAllWeather(weathers: List<Weather>)

    suspend fun deleteWeather(id: Int)

    suspend fun getAllLocations(): MutableList<Location>
}