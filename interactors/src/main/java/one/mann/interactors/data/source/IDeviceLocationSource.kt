package one.mann.interactors.data.source

import one.mann.domain.model.Location

interface IDeviceLocationSource {

    suspend fun getLocation(): Location
}