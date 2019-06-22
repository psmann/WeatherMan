package one.mann.weatherman.api.openweathermap

import retrofit2.http.GET
import retrofit2.http.Query

internal interface OwmService {

    @GET("weather")
    suspend fun getCurrentWeather(
            @Query("lat") latitude: Float,
            @Query("lon") longitude: Float,
            @Query("units") units: String,
            @Query("appid") appId: String
    ): DtoCurrentWeather

    @GET("forecast/daily")
    suspend fun getDailyForecast(
            @Query("lat") latitude: Float,
            @Query("lon") longitude: Float,
            @Query("units") units: String,
            @Query("appid") appId: String
    ): DtoDailyForecast
}