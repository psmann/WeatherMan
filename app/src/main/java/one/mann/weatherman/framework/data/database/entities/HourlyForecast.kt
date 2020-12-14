package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
internal data class HourlyForecast(
        @PrimaryKey(autoGenerate = true) val hourlyId: Int = 0,
        val time: Long,
        val temperature: Float,
        val iconId: Int,
        val sunPosition: Float,
        val cityId: String
)