package one.mann.weatherman.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import one.mann.weatherman.data.WeatherData;
import one.mann.weatherman.model.Main;
import one.mann.weatherman.model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherResult {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "bd7173aa3aec6c2d8f88b500666a116e";
    private static final String UNITS = "metric";
    private WeatherData weatherData;
    private Context context;

    public WeatherResult(Context context) {
        weatherData = new WeatherData(context);
        this.context = context;
    }

    public void getWeatherInfo(final Double[] geoCoordinates) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        final OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
        Call<Weather> weatherCall = openWeatherMapApi.getWeather(geoCoordinates[0], geoCoordinates[1], UNITS, APP_ID);

        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                weatherData.saveProgressBar(false);
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                    return;
                }
                Weather weather = response.body();
                if (weather == null)
                    return;
                saveWeather(weather.getMain(), geoCoordinates);
            }

            @Override
            public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                weatherData.saveProgressBar(false);
                Toast.makeText(context, "Could not connect to the server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWeather(Main main, Double[] location) {
        String coordinates = location[0].toString() + ", " + location[1].toString();
        SharedPreferences.Editor editor = weatherData.getPreferences().edit();
        editor.putString(WeatherData.CURRENT_TEMP, String.valueOf(main.getTemp()));
        editor.putString(WeatherData.MAX_TEMP, String.valueOf(main.getTemp_max()));
        editor.putString(WeatherData.MIN_TEMP, String.valueOf(main.getTemp_min()));
        editor.putString(WeatherData.PRESSURE, String.valueOf(main.getPressure()));
        editor.putString(WeatherData.HUMIDITY, String.valueOf(main.getHumidity()));
        editor.putString(WeatherData.LOCATION, coordinates);
        editor.apply();
    }
}