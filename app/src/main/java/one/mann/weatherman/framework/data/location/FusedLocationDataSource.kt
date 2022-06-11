package one.mann.weatherman.framework.data.location

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import com.google.android.gms.location.*
import kotlinx.coroutines.android.asCoroutineDispatcher
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
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 10 * 1000L
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    client.removeLocationUpdates(this)
                    continuation.resume(
                        Location(
                            listOf(
                                location.latitude.toFloat(),
                                location.longitude.toFloat()
                            )
                        ).truncate()
                    )
                }
            }
        }
        // Handler thread for requestLocationUpdates() since it doesn't seem to run on current thread anymore
        val handlerThread = HandlerThread("LocationUpdate-Thread").apply { start() }
        Handler(handlerThread.looper).asCoroutineDispatcher()
        client.requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)
    }
}