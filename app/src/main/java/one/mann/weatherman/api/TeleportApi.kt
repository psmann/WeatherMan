package one.mann.weatherman.api

import one.mann.weatherman.model.Teleport.TimeZone
import retrofit2.Call
import retrofit2.http.GET

interface TeleportApi {

    @GET("?embed=location:nearest-cities/location:nearest-city/city:timezone")
    fun getTimeZone(): Call<TimeZone>
}