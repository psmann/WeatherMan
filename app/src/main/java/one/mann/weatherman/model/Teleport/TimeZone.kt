package one.mann.weatherman.model.Teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeZone {

    @SerializedName("_embedded")
    @Expose
    var embedded: Embedded? = null

}
