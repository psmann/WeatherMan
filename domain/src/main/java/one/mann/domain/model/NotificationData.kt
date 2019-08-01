package one.mann.domain.model

data class NotificationData(
        val cityName: String,
        val description: String,
        val currentTemp: String,
        val minTemp: String,
        val maxTemp: String,
        val iconId: Int,
        val sunPosition: Float,
        val humidity: String,
        val hour03Time: String,
        val hour03IconId: Int,
        val hour03SunPosition: Float,
        val hour06Time: String,
        val hour06IconId: Int,
        val hour06SunPosition: Float,
        val hour09Time: String,
        val hour09IconId: Int,
        val hour09SunPosition: Float,
        val hour12Time: String,
        val hour12IconId: Int,
        val hour12SunPosition: Float,
        val hour15Time: String,
        val hour15IconId: Int,
        val hour15SunPosition: Float
)