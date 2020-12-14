package one.mann.weatherman.framework.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import one.mann.weatherman.framework.data.database.entities.City
import one.mann.weatherman.framework.data.database.entities.DailyForecast

/* Created by Psmann. */

internal data class CityWithDailyForecasts(
        @Embedded val city: City,
        @Relation(
                parentColumn = "cityId",
                entityColumn = "cityId"
        )
        val dailyForecasts: List<DailyForecast>
) {
    /** Returns a list sorted in ascending order using variable 'date' */
    fun getSortedForecast(): List<DailyForecast> = dailyForecasts.sortedBy { it.date }
}