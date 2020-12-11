package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class CompleteWeather(
        @PrimaryKey(autoGenerate = false) val uuid: String
)