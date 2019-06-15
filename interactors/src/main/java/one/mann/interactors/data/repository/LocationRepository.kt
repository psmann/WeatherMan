package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.interactors.data.source.IApiLocationSource
import one.mann.interactors.data.source.IDeviceLocationSource

class LocationRepository(private val apiLocation: IApiLocationSource,
                         private val deviceLocation: IDeviceLocationSource) {

    suspend fun getLocation(locationType: LocationType): Location {
        return when (locationType) {
            LocationType.API -> getApiLocation()
            LocationType.DEVICE -> getDeviceLocation()
        }
    }

    private suspend fun getDeviceLocation(): Location {
        TODO()
    }

    private suspend fun getApiLocation(): Location {
        TODO()
    }
}