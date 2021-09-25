package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
internal data class CurrentWeather(
    @PrimaryKey(autoGenerate = true) val weatherId: Int = 0,
    val currentTemperature: Float,
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    val description: String,
    val iconId: Int,
    val sunrise: Long,
    val sunset: Long,
    val countryFlag: String,
    val clouds: Int,
    val windSpeed: Float,
    val windDirection: Int,
    val lastUpdated: Long,
    val visibility: Float,
    val dayLength: String,
    val lastChecked: Long,
    val sunPosition: Float,
    val cityId: String
)