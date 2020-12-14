package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
internal data class DailyForecast(
        @PrimaryKey(autoGenerate = true) val dailyId: Int = 0,
        val date: Long,
        val minTemp: Float,
        val maxTemp: Float,
        val iconId: Int,
        val cityId: String
)