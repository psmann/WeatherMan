package one.mann.weatherman.api.teleport

import one.mann.domain.model.location.Location
import one.mann.interactors.data.sources.TimezoneDataSource
import one.mann.weatherman.api.common.mapToString
import javax.inject.Inject

internal class TeleportDataSource @Inject constructor(
        private val teleportService: TeleportService
) : TimezoneDataSource {

    override suspend fun getTimezone(location: Location): String = teleportService
            .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
            .mapToString()

    override suspend fun getAllTimezone(locations: List<Location>): List<String> {
        val timezones: MutableList<String> = mutableListOf()
        for (location in locations) timezones.add(teleportService
                .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
                .mapToString())
        return timezones
    }
}