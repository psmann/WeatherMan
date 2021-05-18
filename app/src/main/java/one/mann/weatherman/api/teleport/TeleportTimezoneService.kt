package one.mann.weatherman.api.teleport

import one.mann.weatherman.api.teleport.dto.Timezone
import retrofit2.http.GET
import retrofit2.http.Path

/* Created by Psmann. */

internal interface TeleportTimezoneService {

    @GET("{lat},{long}/?embed=location:nearest-cities/location:nearest-city/city:timezone")
    suspend fun getTimezone(
        @Path("lat") latitude: String,
        @Path("long") longitude: String
    ): Timezone?
}