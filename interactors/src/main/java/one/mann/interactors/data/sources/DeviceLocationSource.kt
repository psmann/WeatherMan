package one.mann.interactors.data.sources

import one.mann.domain.model.location.Location

interface DeviceLocationSource {

    suspend fun getLocation(): Location
}