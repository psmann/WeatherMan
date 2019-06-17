package one.mann.weatherman.framework.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*

internal class LocationService(context: Context) {

    private var locationCallback: LocationCallback? = null
    private val locationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val currentLocation: Array<Double?> = arrayOfNulls(2)

    @SuppressLint("MissingPermission") // locationProviderClient is already being checked
    fun getLocation(callback: (Array<Double?>) -> Unit) {
        val locationRequest = LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000L)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null)
                    for (location in locationResult.locations) {
                        currentLocation[0] = location.latitude
                        currentLocation[1] = location.longitude
                        locationProviderClient.removeLocationUpdates(this)
                        callback(currentLocation)
                    }
            }
        }
        locationProviderClient.lastLocation.addOnSuccessListener { // Check for last location
            location ->
            if (location != null) { // Use if available
                currentLocation[0] = location.latitude
                currentLocation[1] = location.longitude
                callback(currentLocation)
            } else // Otherwise request for a location update (drains battery)
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
        }
    }
}