package one.mann.weatherman.framework.data.location

import android.annotation.SuppressLint
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import one.mann.domain.logic.truncate
import one.mann.domain.models.location.Location
import one.mann.interactors.data.sources.framework.DeviceLocationSource
import javax.inject.Inject
import kotlin.coroutines.resume

/* Created by Psmann. */

/** Data source for device GPS location */
internal class FusedLocationDataSource @Inject constructor(private val client: FusedLocationProviderClient) :
        DeviceLocationSource {

    @SuppressLint("MissingPermission") // Already being checked
    override suspend fun getLocation(): Location = suspendCancellableCoroutine { continuation ->
        val locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000L)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    client.removeLocationUpdates(this)
                    continuation.resume(Location(listOf(location.latitude.toFloat(), location.longitude.toFloat()))
                            .truncate())
                }
            }
        } // Check for last location if available else request for an update (drains battery)
        client.lastLocation.addOnSuccessListener {
            if (it != null) continuation.resume(Location(listOf(it.latitude.toFloat(), it.longitude.toFloat())).truncate())
            else client.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}