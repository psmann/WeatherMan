package one.mann.weatherman.api.teleport

import one.mann.domain.model.Location
import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.weatherman.api.mapToString

internal class TeleportDataSource : IApiTimezoneSource {

    override suspend fun getTimezone(location: Location): String =
            RetrofitInstance.service
                    .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
                    .mapToString()

    override suspend fun getAllTimezone(locations: List<Location>): List<String> {
        val timezones: MutableList<String> = mutableListOf()
        for(location in locations) timezones.add(RetrofitInstance.service
                .getTimezone(location.coordinates[0].toString(), location.coordinates[1].toString())
                .mapToString())
        return timezones
    }
}