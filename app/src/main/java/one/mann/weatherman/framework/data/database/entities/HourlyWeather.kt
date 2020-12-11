package one.mann.weatherman.framework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Created by Psmann. */

@Entity
data class HourlyWeather(
        @PrimaryKey(autoGenerate = false) val uuid: String,
        val hour03Time: String,
        val hour03Temp: String,
        val hour03IconId: Int,
        val hour03SunPosition: Float,
        val hour06Time: String,
        val hour06Temp: String,
        val hour06IconId: Int,
        val hour06SunPosition: Float,
        val hour09Time: String,
        val hour09Temp: String,
        val hour09IconId: Int,
        val hour09SunPosition: Float,
        val hour12Time: String,
        val hour12Temp: String,
        val hour12IconId: Int,
        val hour12SunPosition: Float,
        val hour15Time: String,
        val hour15Temp: String,
        val hour15IconId: Int,
        val hour15SunPosition: Float,
        val hour18Time: String,
        val hour18Temp: String,
        val hour18IconId: Int,
        val hour18SunPosition: Float,
        val hour21Time: String,
        val hour21Temp: String,
        val hour21IconId: Int,
        val hour21SunPosition: Float
)