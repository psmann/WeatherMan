package one.mann.domain.models.weather

/* Created by Psmann. */

data class City(
        val cityId: String = "",
        val coordinatesLat: Float = 0f,
        val coordinatesLong: Float = 0f,
        val timezone: String = "",
        val timeCreated: Long = 0
)
