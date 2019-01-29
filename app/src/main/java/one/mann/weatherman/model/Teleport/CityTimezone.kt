package one.mann.weatherman.model.Teleport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityTimezone {

    @SerializedName("iana_name")
    @Expose
    var ianaName: String? = null

}
