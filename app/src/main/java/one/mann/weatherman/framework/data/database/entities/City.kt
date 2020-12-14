package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
internal data class City(
        @PrimaryKey(autoGenerate = false) val cityId: String,
        val cityName: String,
        val coordinatesLat: Float,
        val coordinatesLong: Float,
        val timezone: String,
        val timeAdded: Long // Used to order the list
)