package one.mann.domain.models

/* Created by Psmann. */

data class NotificationData(
        val cityName: String = "",
        val description: String = "",
        val currentTemp: Float = 0f,
        val day1MinTemp: Float = 0f,
        val day1MaxTemp: Float = 0f,
        val iconId: Int = 0,
        val sunPosition: Float = 0f,
        val humidity: Int = 0,
        val hour03Time: Long = 0,
        val hour03IconId: Int = 0,
        val hour03SunPosition: Float = 0f,
        val hour06Time: Long = 0,
        val hour06IconId: Int = 0,
        val hour06SunPosition: Float = 0f,
        val hour09Time: Long = 0,
        val hour09IconId: Int = 0,
        val hour09SunPosition: Float = 0f,
        val hour12Time: Long = 0,
        val hour12IconId: Int = 0,
        val hour12SunPosition: Float = 0f,
        val hour15Time: Long = 0,
        val hour15IconId: Int = 0,
        val hour15SunPosition: Float = 0f
)