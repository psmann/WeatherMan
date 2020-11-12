package one.mann.interactors.data.sources.framework

import one.mann.domain.models.location.Location

/* Created by Psmann. */

interface DeviceLocationSource {

    suspend fun getLocation(): Location
}