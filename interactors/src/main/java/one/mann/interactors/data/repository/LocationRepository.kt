package one.mann.interactors.data.repository

import one.mann.domain.model.Location
import one.mann.domain.model.LocationType
import one.mann.interactors.data.source.IDeviceLocationSource

class LocationRepository(private val deviceLocation: IDeviceLocationSource,
                         private val apiLocation: Location?) {

//    suspend fun getLocation(locationType: LocationType): Location {
//        return when (locationType) {
//            LocationType.API -> getApiLocation()
//            LocationType.DEVICE -> getDeviceLocation()
//        }
//    }

    internal suspend fun getDeviceLocation(): Location = deviceLocation.getLocation()

    internal suspend fun getApiLocation(): Location = apiLocation!!
}