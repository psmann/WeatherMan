package one.mann.weatherman.model.Teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Embedded_ {

    @SerializedName("location:nearest-city")
    @Expose
    var locationNearestCity: LocationNearestCity? = null

}
