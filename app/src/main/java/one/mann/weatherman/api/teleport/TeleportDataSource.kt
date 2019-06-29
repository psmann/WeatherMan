package one.mann.weatherman.api.teleport

import one.mann.domain.model.Location
import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.weatherman.api.common.mapToString
import javax.inject.Inject

internal class TeleportDataSource @Inject constructor(
        private val teleportService: TeleportService
) : IApiTimezoneSource {

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