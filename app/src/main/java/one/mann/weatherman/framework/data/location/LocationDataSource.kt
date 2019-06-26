package one.mann.weatherman.framework.data.location

import android.annotation.SuppressLint
import android.app.Application
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import one.mann.domain.model.Location
import one.mann.interactors.data.source.IDeviceLocationSource
import kotlin.coroutines.resume

internal class LocationDataSource(application: Application) : IDeviceLocationSource {

    private val client = LocationServices.getFusedLocationProviderClient(application)

    @SuppressLint("MissingPermission") // Already being checked
    override suspend fun getLocation(): Location =
            suspendCancellableCoroutine { continuation ->
                val locationRequest = LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10 * 1000L)
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            client.removeLocationUpdates(this)
                            continuation.resume(Location(arrayOf(location.latitude.toFloat(),
                                    location.longitude.toFloat()), 1))
                        } // Location id = 1 because this is always the first entry in DB
                    }
                } // Check for last location if available else request for an update (drains battery)
                client.lastLocation.addOnSuccessListener {
                    if (it != null) continuation.resume(
                            Location(arrayOf(it.latitude.toFloat(), it.longitude.toFloat()), 1))
                    else client.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }
}