package one.mann.weatherman.api.openweathermap

import retrofit2.http.GET
import retrofit2.http.Query

internal interface OwmService {

    @GET("weather")
    suspend fun getCurrentWeather(
            @Query("lat") latitude: Float,
            @Query("lon") longitude: Float
    ): DtoCurrentWeather

    @GET("forecast/daily")
    suspend fun getDailyForecast(
            @Query("lat") latitude: Float,
            @Query("lon") longitude: Float
    ): DtoDailyForecast

    @GET("forecast")
    suspend fun getHourlyForecast(
            @Query("lat") latitude: Float,
            @Query("lon") longitude: Float,
            @Query("cnt") count: Int = 7 // Restrict to only next 7 three-hourly forecasts (= 21 hours)
    ): DtoHourlyForecast
}