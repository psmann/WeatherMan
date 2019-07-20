package one.mann.interactors.data.sources

import one.mann.domain.model.Location

interface DeviceLocationSource {

    suspend fun getLocation(): Location
}