package one.mann.weatherman.api.timezonedb.dto

import com.google.gson.annotations.SerializedName

/* Created by Psmann. */

/**
 * DTO for TimeZoneDB API
 * Parameters are nullable to avoid NPEs
 */
internal data class Timezone(
    @SerializedName("zoneName")
    val zoneName: String?
)
