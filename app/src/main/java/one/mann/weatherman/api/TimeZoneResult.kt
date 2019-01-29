package one.mann.weatherman.api

import one.mann.weatherman.model.Teleport.TimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TimeZoneResult {

    fun getTimeZone(latitude: Double?, longitude: Double?) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL + latitude.toString() + "," + longitude.toString() + "/")
                .addConverterFactory(GsonConverterFactory.create()).build()
        val teleportApi = retrofit.create<TeleportApi>(TeleportApi::class.java)
        val timeZoneCall = teleportApi.getTimeZone()

        timeZoneCall.enqueue(object : Callback<TimeZone> {
            override fun onResponse(call: Call<TimeZone>, response: Response<TimeZone>) {
                if (!response.isSuccessful) {
                    return
                }
                val timeZone = response.body() ?: return
                val tz = timeZone.embedded!!.locationNearestCities!![0].embedded!!.locationNearestCity!!
                        .embedded!!.cityTimezone!!.ianaName
            }

            override fun onFailure(call: Call<TimeZone>, t: Throwable) {
            }
        })

    }

    companion object {
        private const val BASE_URL = "https://api.teleport.org/api/locations/"
    }
}