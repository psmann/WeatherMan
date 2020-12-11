package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class HourlyForecast(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val weatherId: String,
        val time: String,
        val temperature: String,
        val iconId: Int,
        val sunPosition: Float
)