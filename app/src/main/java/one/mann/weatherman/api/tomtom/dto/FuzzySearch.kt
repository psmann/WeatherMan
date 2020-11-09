package one.mann.weatherman.api.tomtom.dto

/* Created by Psmann. */

/**
 * Data Transfer Object (model) for TomTom Fuzzy Search API
 * All parameters are nullable to maintain Kotlin null-safety
 */
internal data class FuzzySearch(val result: List<Result>?) {

    data class Result(
            val address: Address?,
            val position: Position?
    )

    data class Address(val freeformAddress: String?)

    data class Position(
            val lat: Float?,
            val lon: Float?
    )
}