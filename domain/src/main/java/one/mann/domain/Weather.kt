package one.mann.domain

data class Weather(val currentTemp: Float,
                   val pressure: Float,
                   val humidity: Float,
                   val description: String?,
                   val icon: String?,
                   val sunrise: Long,
                   val sunset: Long,
                   val country: String?,
                   val clouds: Float,
                   val windSpeed: Float,
                   val windDir: Float,
                   val name: String?,
                   val date: Long,
                   val visibility: Float)