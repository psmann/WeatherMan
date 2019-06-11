package one.mann.weatherman.api.teleport

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface TeleportService {

    @GET("{lat},{long}/?embed=location:nearest-cities/location:nearest-city/city:timezone")
    fun getTimeZone(@Path("lat") latitude: String,
                    @Path("long") longitude: String
    ): Call<DtoTimezone>

    @GET("{lat},{long}/?embed=location:nearest-cities/location:nearest-city/city:timezone")
    suspend fun getTimeZoneData(@Path("lat") latitude: String,
                                @Path("long") longitude: String
    ): DtoTimezone
}