package one.mann.weatherman.ui.common.models

/* Created by Psmann. */

internal data class NotificationData(
    val cityName: String = "",
    val description: String = "",
    val currentTemp: String = "",
    val day1MinTemp: String = "",
    val day1MaxTemp: String = "",
    val iconId: Int = 0,
    val sunPosition: Float = 0f,
    val humidity: String = "",
    val hour03Time: String = "",
    val hour03IconId: Int = 0,
    val hour03SunPosition: Float = 0f,
    val hour06Time: String = "",
    val hour06IconId: Int = 0,
    val hour06SunPosition: Float = 0f,
    val hour09Time: String = "",
    val hour09IconId: Int = 0,
    val hour09SunPosition: Float = 0f,
    val hour12Time: String = "",
    val hour12IconId: Int = 0,
    val hour12SunPosition: Float = 0f,
    val hour15Time: String = "",
    val hour15IconId: Int = 0,
    val hour15SunPosition: Float = 0f
)