package one.mann.weatherman.framework.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class Weather(
        @PrimaryKey val id: Int?, // when null SQLite's default ROWID increment system is used
        // Current Weather
        val cityName: String,
        val currentTemp: String,
        val feelsLike: String,
        val pressure: String,
        val humidity: String,
        val description: String,
        val iconId: Int,
        val sunrise: String,
        val sunset: String,
        val countryFlag: String,
        val clouds: String,
        val windSpeed: String,
        val windDirection: String,
        val lastUpdated: String,
        val visibility: String,
        val dayLength: String,
        val lastChecked: String,
        val sunPosition: Float,
        val minTemp: String,
        val maxTemp: String,
        // Daily Forecast
        val day1Date: String,
        val day1MinTemp: String,
        val day1MaxTemp: String,
        val day1IconId: Int,
        val day2Date: String,
        val day2MinTemp: String,
        val day2MaxTemp: String,
        val day2IconId: Int,
        val day3Date: String,
        val day3MinTemp: String,
        val day3MaxTemp: String,
        val day3IconId: Int,
        val day4Date: String,
        val day4MinTemp: String,
        val day4MaxTemp: String,
        val day4IconId: Int,
        val day5Date: String,
        val day5MinTemp: String,
        val day5MaxTemp: String,
        val day5IconId: Int,
        val day6Date: String,
        val day6MinTemp: String,
        val day6MaxTemp: String,
        val day6IconId: Int,
        val day7Date: String,
        val day7MinTemp: String,
        val day7MaxTemp: String,
        val day7IconId: Int,
        // Location
        val coordinatesLat: Float,
        val coordinatesLong: Float,
        val locationString: String
)