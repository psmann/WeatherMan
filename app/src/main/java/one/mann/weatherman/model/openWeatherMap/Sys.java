package one.mann.weatherman.model.openWeatherMap;

public class Sys {
    private long sunrise, sunset;
    private String country;

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getCountry() {
        return country;
    }
}