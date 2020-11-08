package one.mann.weatherman.api.tomtom.dto

/* Created by Psmann. */

/** Model class for API TomTom Fuzzy Search
 * All parameters are set to nullable to make sure Kotlin null-safety never breaks
 */
internal data class FuzzySearch(val result: List<Result>?) {

    data class Result(
            val entityType: String?,
            val address: Address?,
            val position: Position
    ) {

        data class Address(
                val country: String?,
                val countrySubdivision: String?,
                val countrySecondarySubdivision: String?,
                val countryTertiarySubdivision: String?,
                val municipality: String?,
                val municipalitySubdivision: String?
        )

        data class Position(
                val lat: Float?,
                val lon: Float?
        )
    }
}