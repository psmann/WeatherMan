package one.mann.interactors.data.source

import one.mann.domain.model.Location

interface DeviceLocationSource {

    suspend fun getLocation(): Location
}