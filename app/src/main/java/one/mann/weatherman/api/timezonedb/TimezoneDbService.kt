package one.mann.weatherman.api.timezonedb

import one.mann.weatherman.api.timezonedb.dto.Timezone
import retrofit2.http.GET
import retrofit2.http.Query

/* Created by Psmann. */

internal interface TimezoneDbService {

    companion object {
        private const val FORMAT = "json"
        private const val BY = "position"
        private const val FIELDS = "zoneName"
    }

    @GET("get-time-zone")
    suspend fun getTimezone(
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("format") format: String = FORMAT,
        @Query("by") by: String = BY,
        @Query("fields") fields: String = FIELDS
    ): Timezone?
}
