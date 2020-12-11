package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class DailyWeather(
        @PrimaryKey(autoGenerate = false) val uuid: String,
        val day1Date: String,
        val day1MinTemp: String,
        val day1MaxTemp: String,
        val day1IconId: Int,
        val day2Date: String,
        val day2MinTemp: String,
        val day2MaxTemp: String,
        val day2IconId: Int,
        val day3Date: String,
        val day3MinTemp: String,
        val day3MaxTemp: String,
        val day3IconId: Int,
        val day4Date: String,
        val day4MinTemp: String,
        val day4MaxTemp: String,
        val day4IconId: Int,
        val day5Date: String,
        val day5MinTemp: String,
        val day5MaxTemp: String,
        val day5IconId: Int,
        val day6Date: String,
        val day6MinTemp: String,
        val day6MaxTemp: String,
        val day6IconId: Int,
        val day7Date: String,
        val day7MinTemp: String,
        val day7MaxTemp: String,
        val day7IconId: Int
)