package one.mann.weatherman.api.teleport

import com.google.gson.annotations.SerializedName

internal class DtoTimezone(
        @SerializedName("_embedded")
        var embedded1: Embedded1
) {
    data class CityTimezone(
            @SerializedName("iana_name")
            var ianaName: String
    )

    data class Embedded1(
            @SerializedName("location:nearest-cities")
            var locationNearestCities: List<LocationNearestCities>
    )

    data class Embedded2(
            @SerializedName("location:nearest-city")
            var locationNearestCity: LocationNearestCity
    )

    data class Embedded3(
            @SerializedName("city:timezone")
            var cityTimezone: CityTimezone
    )

    data class LocationNearestCities(
            @SerializedName("_embedded")
            var embedded2: Embedded2
    )

    data class LocationNearestCity(
            @SerializedName("_embedded")
            var embedded3: Embedded3
    )
}