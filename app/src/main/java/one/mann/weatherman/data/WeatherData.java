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
    public static final String LOADING_BAR = "LOADING_BAR";
    public static final String SUNRISE = "SUNRISE";
    public static final String SUNSET = "SUNSET";
    public static final String COUNTRY_FLAG = "COUNTRY_FLAG";
    public static final String CLOUDS = "CLOUDS";
    public static final String WIND_SPEED = "WIND_SPEED";
    public static final String WIND_DIRECTION = "WIND_DIRECTION";
    public static final String VISIBILITY = "VISIBILITY";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ICON_CODE = "ICON_CODE";
    public static final String UI_VISIBILITY = "UI_VISIBILITY";
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

    public void saveLoadingBar(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOADING_BAR, value);
        editor.apply();
    }

    public boolean getLoadingBar() {
        return preferences.getBoolean(LOADING_BAR, false);
    }

    public boolean getUiVisibility() {
        return preferences.getBoolean(UI_VISIBILITY, false);
    }
}