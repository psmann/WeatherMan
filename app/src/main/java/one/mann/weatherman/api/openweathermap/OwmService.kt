package one.mann.weatherman.api.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface OwmService {

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double,
                   @Query("lon") longitude: Double,
                   @Query("units") units: String,
                   @Query("appid") appId: String
    ): Call<DtoWeather>

    @GET("forecast/daily")
    fun getForecast(@Query("lat") latitude: Double,
                    @Query("lon") longitude: Double,
                    @Query("units") units: String,
                    @Query("appid") appId: String
    ): Call<DtoDailyForecast>

    @GET("weather")
    suspend fun getWeatherData(@Query("lat") latitude: Double,
                               @Query("lon") longitude: Double,
                               @Query("units") units: String,
                               @Query("appid") appId: String
    ): DtoWeather

    @GET("forecast/daily")
    suspend fun getForecastData(@Query("lat") latitude: Double,
                                @Query("lon") longitude: Double,
                                @Query("units") units: String,
                                @Query("appid") appId: String
    ): DtoDailyForecast
}