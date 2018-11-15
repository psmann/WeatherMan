package one.mann.weatherman.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import one.mann.weatherman.R;
import one.mann.weatherman.data.WeatherData;
import one.mann.weatherman.model.openWeatherMap.Clouds;
import one.mann.weatherman.model.openWeatherMap.Main;
import one.mann.weatherman.model.openWeatherMap.Sys;
import one.mann.weatherman.model.openWeatherMap.CurrentWeather;
import one.mann.weatherman.model.openWeatherMap.Weather;
import one.mann.weatherman.model.openWeatherMap.Wind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherResult {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String ICON_URL = "http://openweathermap.org/img/w/";
    private static final String APP_ID = "bd7173aa3aec6c2d8f88b500666a116e";
    private static final String DATE_PATTERN = "d MMM, h:mm aa";
    private static final String HOURS_PATTERN = "H 'Hours and' m 'Minutes'";
    private static final String UNITS = "metric";
    private static final String ICON_EXTENSION = ".png";
    private static final String CELSIUS = " C";
    private static final String HECTOPASCAL = " hPa";
    private static final String PERCENT = " %";
    private static final String METERS = " m";
    private static final String METERS_PER_SECOND = " m/s";
    private static final String DEGREES = " °";
    private static final int FLAG_OFFSET = 0x1F1E6; // Regional Indicator Symbol for letter A
    private static final int ASCII_OFFSET = 0x41; // Uppercase letter A
    private WeatherData weatherData;
    private Context context;
    private DateFormat dateFormat, hoursFormat;

    public WeatherResult(Context context) {
        weatherData = new WeatherData(context);
        this.context = context;
        dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        hoursFormat = new SimpleDateFormat(HOURS_PATTERN, Locale.getDefault());
        hoursFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Remove time offset
    }

    public void weatherCall(final Double[] geoCoordinates) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        final OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
        Call<CurrentWeather> weatherCall = openWeatherMapApi.getWeather(geoCoordinates[0], geoCoordinates[1], UNITS, APP_ID);

        weatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {
                weatherData.saveLoadingBar(false);
                if (!response.isSuccessful()) {
                    Toast.makeText(context, R.string.server_not_found, Toast.LENGTH_SHORT).show();
                    return;
                }
                CurrentWeather currentWeather = response.body();
                if (currentWeather == null)
                    return;
                saveWeather(currentWeather.getMain(), currentWeather.getSys(), currentWeather.getWind(),
                        currentWeather.getClouds(), currentWeather.getWeather()[0], geoCoordinates,
                        currentWeather.getName(), currentWeather.getDt(), currentWeather.getVisibility());
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {
                weatherData.saveLoadingBar(false);
                Toast.makeText(context, R.string.server_not_found, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String countryCodeToEmoji(String code) {
        int firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + FLAG_OFFSET;
        int secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + FLAG_OFFSET;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    private String lengthOfDay(long sunrise, long sunset) {
        long length = sunset - sunrise;
        return String.valueOf(hoursFormat.format(new Date(length * 1000))); // Convert to nanosecond
    }

    private void saveWeather(Main main, Sys sys, Wind wind, Clouds clouds, Weather weather, Double[] location,
                             String name, long dt, long visibility) {
        String coordinates = location[0].toString() + ", " + location[1].toString();
        SharedPreferences.Editor editor = weatherData.getPreferences().edit();
        editor.putString(WeatherData.CURRENT_TEMP, String.valueOf(main.getTemp()) + CELSIUS);
        editor.putString(WeatherData.MAX_TEMP, String.valueOf(main.getTemp_max()) + CELSIUS);
        editor.putString(WeatherData.MIN_TEMP, String.valueOf(main.getTemp_min()) + CELSIUS);
        editor.putString(WeatherData.PRESSURE, String.valueOf(main.getPressure()) + HECTOPASCAL);
        editor.putString(WeatherData.HUMIDITY, String.valueOf(main.getHumidity()) + PERCENT);
        editor.putString(WeatherData.LOCATION, coordinates);
        editor.putString(WeatherData.LATITUDE, String.valueOf(location[0]));
        editor.putString(WeatherData.LONGITUDE, String.valueOf(location[1]));
        editor.putString(WeatherData.CITY_NAME, name);
        editor.putString(WeatherData.LAST_UPDATED, String.valueOf(dateFormat.format(new Date(dt * 1000))));
        editor.putString(WeatherData.LAST_CHECKED, String.valueOf(dateFormat.format(new Date(System.currentTimeMillis()))));
        editor.putString(WeatherData.SUNRISE, String.valueOf(dateFormat.format(new Date(sys.getSunrise() * 1000))));
        editor.putString(WeatherData.SUNSET, String.valueOf(dateFormat.format(new Date(sys.getSunset() * 1000))));
        editor.putString(WeatherData.DAY_LENGTH, lengthOfDay(sys.getSunrise(), sys.getSunset()));
        editor.putString(WeatherData.COUNTRY_FLAG, countryCodeToEmoji(String.valueOf(sys.getCountry())));
        editor.putString(WeatherData.CLOUDS, String.valueOf(clouds.getAll()) + PERCENT);
        editor.putString(WeatherData.WIND_SPEED, String.valueOf(wind.getSpeed()) + METERS_PER_SECOND);
        editor.putString(WeatherData.WIND_DIRECTION, String.valueOf(wind.getDeg()) + DEGREES);
        editor.putString(WeatherData.VISIBILITY, String.valueOf(visibility) + METERS);
        editor.putString(WeatherData.DESCRIPTION, String.valueOf(weather.getMain()));
        editor.putString(WeatherData.ICON_CODE, ICON_URL + String.valueOf(weather.getIcon()) + ICON_EXTENSION);
        editor.putBoolean(WeatherData.UI_VISIBILITY, true);
        editor.apply();
    }
}