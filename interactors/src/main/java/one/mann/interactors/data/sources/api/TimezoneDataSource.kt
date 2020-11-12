package one.mann.interactors.data.sources.api

import one.mann.domain.models.location.Location

/* Created by Psmann. */

interface TimezoneDataSource {

    suspend fun getTimezone(location: Location): String

    suspend fun getAllTimezone(locations: List<Location>): List<String>
}