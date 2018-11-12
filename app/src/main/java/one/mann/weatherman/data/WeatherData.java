package one.mann.weatherman.data;

import android.content.Context;
import android.content.SharedPreferences;

public final class WeatherData {

    public static final String CURRENT_TEMP = "CURRENT_TEMP";
    public static final String MAX_TEMP = "MAX_TEMP";
    public static final String MIN_TEMP = "MIN_TEMP";
    public static final String PRESSURE = "PRESSURE";
    public static final String HUMIDITY = "HUMIDITY";
    public static final String LOCATION = "LOCATION";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String LAST_CHECKED = "LAST_CHECKED";
    public static final String LAST_UPDATED = "LAST_UPDATED";
    private static final String PROGRESS_BAR = "PROGRESS_BAR";
    public static final String SUNRISE = "SUNRISE";
    public static final String SUNSET = "SUNSET";
    public static final String CLOUDS = "CLOUDS";
    public static final String WIND_SPEED = "WIND_SPEED";
    public static final String WIND_DIRECTION = "WIND_DIRECTION";
    public static final String VISIBILITY = "VISIBILITY";
    private SharedPreferences preferences;

    public WeatherData(Context context) {
        preferences = context.getSharedPreferences("WEATHER_DATA", Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getWeatherData(String key) {
        return preferences.getString(key, "");
    }

    public void saveProgressBar(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PROGRESS_BAR, value);
        editor.apply();
    }

    public boolean getProgressBar() {
        return preferences.getBoolean(PROGRESS_BAR, false);
    }
}