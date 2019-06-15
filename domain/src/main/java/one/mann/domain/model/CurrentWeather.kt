package one.mann.domain.model

data class CurrentWeather(val cityName: String,
                          val currentTemperature: Float,
                          val pressure: Float,
                          val humidity: Float,
                          val description: String,
                          val icon: String,
                          var sunrise: Long,
                          var sunset: Long,
                          val countryFlag: String,
                          val clouds: Float,
                          val windSpeed: Float,
                          val windDirection: Float,
                          var lastUpdated: Long,
                          val visibility: Float
)