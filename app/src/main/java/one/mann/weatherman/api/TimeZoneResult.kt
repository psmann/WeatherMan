package one.mann.weatherman.api

import one.mann.weatherman.api.openweathermap.WeatherDto
import one.mann.weatherman.api.teleport.TeleportApi
import one.mann.weatherman.api.teleport.TimeZoneDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class TimeZoneResult(private val timeZoneListener: TimeZoneListener) {

    companion object {
        private const val BASE_URL = "https://api.teleport.org/api/locations/"
    }

    fun getTimeZone(latitude: Double?, longitude: Double?, cityPref: String, dto: WeatherDto) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL + latitude.toString() + "," + longitude.toString() + "/")
                .addConverterFactory(GsonConverterFactory.create()).build()
        val teleportApi = retrofit.create<TeleportApi>(TeleportApi::class.java)
        val timeZoneCall = teleportApi.getTimeZone()

        timeZoneCall.enqueue(object : Callback<TimeZoneDto> {
            override fun onResponse(call: Call<TimeZoneDto>, response: Response<TimeZoneDto>) {
                if (!response.isSuccessful) {
                    return
                }
                val timeZone = response.body() ?: return
                val tz = timeZone.embedded!!.locationNearestCities!![0].embedded!!.locationNearestCity!!
                        .embedded!!.cityTimezone!!.ianaName
                timeZoneListener.saveTimeZoneValue(tz!!, cityPref, dto)
            }

            override fun onFailure(call: Call<TimeZoneDto>, t: Throwable) {
            }
        })
    }

    interface TimeZoneListener {
        fun saveTimeZoneValue(tz: String, cityPref: String, dto: WeatherDto)
    }
}