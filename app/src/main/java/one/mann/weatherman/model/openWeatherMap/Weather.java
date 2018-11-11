package one.mann.weatherman.model.openWeatherMap;

public class Weather {
    private Main main;
    private Sys sys;
    private Wind wind;
    private Clouds clouds;
    private String name;
    private long dt;
    private int visibility;

    public Main getMain() {
        return main;
    }

    public Sys getSys() {
        return sys;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public String getName() {
        return name;
    }

    public long getDt() {
        return dt;
    }

    public int getVisibility() {
        return visibility;
    }
}