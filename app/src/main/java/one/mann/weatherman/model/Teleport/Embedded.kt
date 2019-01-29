package one.mann.weatherman.model.Teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Embedded {

    @SerializedName("location:nearest-cities")
    @Expose
    var locationNearestCities: List<LocationNearestCities>? = null

}
