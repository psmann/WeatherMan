package one.mann.weatherman.api.tomtom.dto

/* Created by Psmann. */

/**
 * Data Transfer Object (model) for TomTom Fuzzy Search API
 * All parameters are nullable to maintain Kotlin null-safety
 */
internal data class FuzzySearch(val results: List<Results>?) {

    data class Results(
            val address: Address?,
            val position: Position?
    )

    data class Address(
            val freeformAddress: String?,
            val countryCode: String?
    )

    data class Position(
            val lat: Float?,
            val lon: Float?
    )
}