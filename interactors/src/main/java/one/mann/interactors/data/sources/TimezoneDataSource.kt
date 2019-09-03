package one.mann.interactors.data.sources

import one.mann.domain.model.location.Location

interface TimezoneDataSource {

    suspend fun getTimezone(location: Location): String

    suspend fun getAllTimezone(locations: List<Location>): List<String>
}