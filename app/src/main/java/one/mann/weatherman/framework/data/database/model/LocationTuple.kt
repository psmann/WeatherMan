package one.mann.weatherman.framework.data.database.model

import androidx.room.ColumnInfo

internal data class LocationTuple(
        @ColumnInfo(name = "coordinatesLat") val coordinatesLat: Float,
        @ColumnInfo(name = "coordinatesLong") val coordinatesLong: Float,
        @ColumnInfo(name = "id") val id: Int
)