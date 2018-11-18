package one.mann.weatherman.api

import one.mann.weatherman.model.openWeatherMap.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double?, @Query("lon") longitude: Double?,
                   @Query("units") units: String, @Query("appid") appId: String): Call<CurrentWeather>
}