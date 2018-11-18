package one.mann.weatherman.model

import android.annotation.SuppressLint
import android.content.Context

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GpsLocation(context: Context, private val geoCoordinates: GeoCoordinates) {

    private var locationCallback: LocationCallback? = null
    private val locationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val currentLocation: Array<Double?> = arrayOfNulls(2)

    @SuppressLint("MissingPermission") // locationProviderClient is being checked before this method is called
    fun getLocation() {
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval((10 * 1000).toLong())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null)
                    for (location in locationResult.locations) {
                        currentLocation[0] = location.latitude
                        currentLocation[1] = location.longitude
                        locationProviderClient.removeLocationUpdates(this)
                        geoCoordinates.getCoordinates(currentLocation)
                    }
            }
        }
        locationProviderClient.lastLocation.addOnSuccessListener { // Check for last location
            location ->
            if (location != null) { // Use if available
                currentLocation[0] = location.latitude
                currentLocation[1] = location.longitude
                geoCoordinates.getCoordinates(currentLocation)
            } else
            // Otherwise request for a location update (drains battery)
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
        }
    }

    interface GeoCoordinates {
        fun getCoordinates(location: Array<Double?>)
    }
}