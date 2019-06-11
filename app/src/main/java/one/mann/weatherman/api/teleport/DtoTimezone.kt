package one.mann.weatherman.api.teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class DtoTimezone(
        @SerializedName("_embedded")
        @Expose
        var embedded: Embedded? = null
) {
    data class CityTimezone(
            @SerializedName("iana_name")
            @Expose
            var ianaName: String?
    )

    data class Embedded(
            @SerializedName("location:nearest-cities")
            @Expose
            var locationNearestCities: List<LocationNearestCities>?
    )

    data class Embedded_(
            @SerializedName("location:nearest-city")
            @Expose
            var locationNearestCity: LocationNearestCity?
    )

    data class Embedded__(
            @SerializedName("city:timezone")
            @Expose
            var cityTimezone: CityTimezone?
    )

    data class LocationNearestCities(
            @SerializedName("_embedded")
            @Expose
            var embedded: Embedded_?
    )

    data class LocationNearestCity(
            @SerializedName("_embedded")
            @Expose
            var embedded: Embedded__?
    )
}