package one.mann.weatherman.framework.data.database.model

import androidx.room.ColumnInfo

internal data class NotificationTuple(
        @ColumnInfo(name = "cityName") val cityName: String,
        @ColumnInfo(name = "currentTemp") val currentTemp: String,
        @ColumnInfo(name = "description") val description: String
)