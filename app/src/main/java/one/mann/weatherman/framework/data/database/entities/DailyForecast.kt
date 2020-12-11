package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class DailyForecast(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val weatherId: String,
        val date: String,
        val minimumTemperature: String,
        val maximumTemperature: String,
        val iconId: Int,
)