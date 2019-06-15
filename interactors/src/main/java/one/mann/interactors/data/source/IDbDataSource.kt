package one.mann.interactors.data.source

import one.mann.domain.model.Weather

interface IDbDataSource {

    suspend fun isDbEmpty(): Boolean

    suspend fun insertWeather(weather: Weather)

    suspend fun getAllWeather(): List<Weather>

    suspend fun updateAllWeather()

    suspend fun deleteWeather(id: Int)
}