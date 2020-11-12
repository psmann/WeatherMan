package one.mann.domain.models

/* Created by Psmann. */

data class CitySearchResult(
        val cityLine1: String = "",
        val cityLine2: String = "",
        val latitude: Float = 0f,
        val longitude: Float = 0f,
        val countryFlag: String = ""
)