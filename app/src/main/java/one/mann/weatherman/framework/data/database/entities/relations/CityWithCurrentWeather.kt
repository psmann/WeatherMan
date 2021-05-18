package one.mann.weatherman.framework.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import one.mann.weatherman.framework.data.database.entities.City
import one.mann.weatherman.framework.data.database.entities.CurrentWeather

/* Created by Psmann. */

internal data class CityWithCurrentWeather(
    @Embedded val city: City,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )
    val currentWeather: CurrentWeather
)