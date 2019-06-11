package one.mann.domain

data class DailyForecast(var date: Long,
                         var min: Float,
                         var max: Float,
                         var description: String,
                         var icon: String
)