package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class LocationCoordinates(
        @PrimaryKey(autoGenerate = false)
        val uuid: String,
        val position: Int,
        val coordinatesLat: Float,
        val coordinatesLong: Float
)
