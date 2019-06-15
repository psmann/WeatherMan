package one.mann.weatherman.framework.data.database

import androidx.room.ColumnInfo

data class LocationTuple(
        @ColumnInfo(name = "coordinatesLat") val coordinatesLat: Float,
        @ColumnInfo(name = "coordinatesLong") val coordinatesLong: Float
)