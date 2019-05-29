package one.mann.weatherman.api.teleport

import retrofit2.Call
import retrofit2.http.GET

internal interface TeleportApi {

    @GET("?embed=location:nearest-cities/location:nearest-city/city:timezone")
    fun getTimeZone(): Call<TimeZoneDto>
}