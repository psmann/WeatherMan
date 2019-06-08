package one.mann.weatherman.api.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface OpenWeatherMapApi {

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double?,
                   @Query("lon") longitude: Double?,
                   @Query("units") units: String,
                   @Query("appid") appId: String
    ): Call<WeatherDto>

    @GET("daily")
    fun getForecast(@Query("lat") latitude: Double?,
                    @Query("lon") longitude: Double?,
                    @Query("units") units: String,
                    @Query("appid") appId: String
    ): Call<DailyForecastDto>
}