package one.mann.weatherman.framework.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import one.mann.weatherman.framework.data.database.entities.City
import one.mann.weatherman.framework.data.database.entities.HourlyForecast

/* Created by Psmann. */

internal data class CityWithHourlyForecasts(
    @Embedded val city: City,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )
    val hourlyForecasts: List<HourlyForecast>
) {
    /** Returns a list sorted in ascending order using variable 'time' */
    fun getSortedForecast(): List<HourlyForecast> = hourlyForecasts.sortedBy { it.time }
}