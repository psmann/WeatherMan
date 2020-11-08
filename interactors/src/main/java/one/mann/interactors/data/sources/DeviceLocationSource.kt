package one.mann.interactors.data.sources

import one.mann.domain.model.location.Location

/* Created by Psmann. */

interface DeviceLocationSource {

    suspend fun getLocation(): Location
}