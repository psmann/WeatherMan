package one.mann.interactors.data.source

import one.mann.domain.model.Location

interface IApiTimezoneSource {

    suspend fun getTimezone(location: Location): String

    suspend fun getAllTimezone(locations: List<Location>): List<String>
}