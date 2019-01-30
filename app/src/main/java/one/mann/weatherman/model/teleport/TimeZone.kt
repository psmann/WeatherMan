package one.mann.weatherman.model.teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeZone {

    @SerializedName("_embedded")
    @Expose
    var embedded: Embedded? = null

}
