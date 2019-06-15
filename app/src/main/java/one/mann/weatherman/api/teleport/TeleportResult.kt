package one.mann.weatherman.api.teleport

import one.mann.weatherman.api.openweathermap.DtoCurrentWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class TeleportResult(private val timeZoneListener: TimeZoneListener) {

    companion object {
        private const val BASE_URL = "https://api.teleport.org/api/locations/"
    }

    fun getTimeZone(latitude: Double?, longitude: Double?, cityPref: String, dto: DtoCurrentWeather) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val teleportApi = retrofit.create<TeleportService>(TeleportService::class.java)
        val timeZoneCall = teleportApi.getTimeZone(latitude.toString(), longitude.toString())

        timeZoneCall.enqueue(object : Callback<DtoTimezone> {
            override fun onResponse(call: Call<DtoTimezone>, response: Response<DtoTimezone>) {
                if (!response.isSuccessful) {
                    return
                }
                val timeZone = response.body() ?: return
                val tz = timeZone.embedded
                        .locationNearestCities[0]
                        .embedded
                        .locationNearestCity
                        .embedded
                        .cityTimezone
                        .ianaName
                timeZoneListener.saveTimeZoneValue(tz, cityPref, dto)
            }

            override fun onFailure(call: Call<DtoTimezone>, t: Throwable) {
            }
        })
    }

    interface TimeZoneListener {
        fun saveTimeZoneValue(tz: String, cityPref: String, dto: DtoCurrentWeather)
    }
}