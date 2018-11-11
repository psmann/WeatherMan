package one.mann.weatherman.model.openWeatherMap;

public class Weather {
    private Main main;
    private String name;
    private long dt;

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public long getDt() {
        return dt;
    }
}