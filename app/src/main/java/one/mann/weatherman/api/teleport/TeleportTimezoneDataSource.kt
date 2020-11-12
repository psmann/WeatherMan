package one.mann.weatherman.api.teleport

import one.mann.domain.models.location.Location
import one.mann.interactors.data.sources.api.TimezoneDataSource
import one.mann.weatherman.api.common.mapToString
import javax.inject.Inject

/* Created by Psmann. */

internal class TeleportTimezoneDataSource @Inject constructor(
        private val teleportTimezoneService: TeleportTimezoneService
) : TimezoneDataSource {

    override suspend fun getTimezone(location: Location): String = teleportTimezoneService
            .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
            .mapToString()

    override suspend fun getAllTimezone(locations: List<Location>): List<String> {
        val timezones: MutableList<String> = mutableListOf()
        for (location in locations) timezones.add(teleportTimezoneService
                .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
                .mapToString())
        return timezones
    }
}