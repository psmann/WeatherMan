package one.mann.weatherman.api;

import one.mann.weatherman.model.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {

    @GET("weather")
    Call<Weather> getWeather(@Query("lat") Double latitude, @Query("lon") Double longitude,
                             @Query("units") String units, @Query("appid") String appId);
}