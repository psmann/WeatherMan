package one.mann.weatherman.api.teleport

import one.mann.interactors.data.source.IApiTimezoneSource
import one.mann.weatherman.api.mapToString

internal class TeleportDataSource : IApiTimezoneSource {

    override suspend fun getTimezone(location: Array<Float>): String =
            RetrofitInstance.service
                    .getTimeZoneData(location[0].toString(), location[1].toString())
                    .mapToString()
}