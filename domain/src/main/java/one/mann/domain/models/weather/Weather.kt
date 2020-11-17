package one.mann.domain.models.weather

/* Created by Psmann. */

data class Weather(
        // Current Weather
        val id: Int? = null,
        val cityName: String = "",
        val currentTemp: String = "",
        val feelsLike: String = "",
        val pressure: String = "",
        val humidity: String = "",
        val description: String = "",
        val iconId: Int = 0,
        val sunrise: String = "",
        val sunset: String = "",
        val countryFlag: String = "",
        val clouds: String = "",
        val windSpeed: String = "",
        val windDirection: String = "0",
        val lastUpdated: String = "",
        val visibility: String = "",
        val dayLength: String = "",
        val lastChecked: String = "",
        val sunPosition: Float = 0f,
        // Daily Forecast
        val day1Date: String = "",
        val day1MinTemp: String = "",
        val day1MaxTemp: String = "",
        val day1IconId: Int = 0,
        val day2Date: String = "",
        val day2MinTemp: String = "",
        val day2MaxTemp: String = "",
        val day2IconId: Int = 0,
        val day3Date: String = "",
        val day3MinTemp: String = "",
        val day3MaxTemp: String = "",
        val day3IconId: Int = 0,
        val day4Date: String = "",
        val day4MinTemp: String = "",
        val day4MaxTemp: String = "",
        val day4IconId: Int = 0,
        val day5Date: String = "",
        val day5MinTemp: String = "",
        val day5MaxTemp: String = "",
        val day5IconId: Int = 0,
        val day6Date: String = "",
        val day6MinTemp: String = "",
        val day6MaxTemp: String = "",
        val day6IconId: Int = 0,
        val day7Date: String = "",
        val day7MinTemp: String = "",
        val day7MaxTemp: String = "",
        val day7IconId: Int = 0,
        // Hourly Forecast
        val hour03Time: String = "",
        val hour03Temp: String = "",
        val hour03IconId: Int = 0,
        val hour03SunPosition: Float = 0f,
        val hour06Time: String = "",
        val hour06Temp: String = "",
        val hour06IconId: Int = 0,
        val hour06SunPosition: Float = 0f,
        val hour09Time: String = "",
        val hour09Temp: String = "",
        val hour09IconId: Int = 0,
        val hour09SunPosition: Float = 0f,
        val hour12Time: String = "",
        val hour12Temp: String = "",
        val hour12IconId: Int = 0,
        val hour12SunPosition: Float = 0f,
        val hour15Time: String = "",
        val hour15Temp: String = "",
        val hour15IconId: Int = 0,
        val hour15SunPosition: Float = 0f,
        val hour18Time: String = "",
        val hour18Temp: String = "",
        val hour18IconId: Int = 0,
        val hour18SunPosition: Float = 0f,
        val hour21Time: String = "",
        val hour21Temp: String = "",
        val hour21IconId: Int = 0,
        val hour21SunPosition: Float = 0f,
        // Location
        val coordinatesLat: Float = 0f,
        val coordinatesLong: Float = 0f,
        val locationString: String = ""
)