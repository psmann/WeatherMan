package one.mann.weatherman.ui.base

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import one.mann.weatherman.ui.common.checkNetworkConnection

internal abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val locationRequest = 1011

        private var locationPermissionListener: (Boolean) -> Unit = {}

        private var networkLocationServiceListener: (LocationResponse) -> Unit = {}

        private val locationRequestBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequest) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
                locationPermissionListener(true)
            else locationPermissionListener(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            locationRequest ->
                when (resultCode) {
                    Activity.RESULT_OK -> networkLocationServiceListener(LocationResponse.ENABLED)
                    Activity.RESULT_CANCELED -> networkLocationServiceListener(LocationResponse.DISABLED)
                }
        }
    }

    // Gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION)
    protected fun handleLocationPermission(result: (Boolean) -> Unit) {
        locationPermissionListener = result
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), locationRequest)
        else locationPermissionListener(true)
    }

    // Check status of location services and handle in lambda
    protected fun checkLocationService(result: (LocationResponse) -> Unit) {
        if (!checkNetworkConnection()) {
            result(LocationResponse.NO_NETWORK)
            return
        }
        networkLocationServiceListener = result
        LocationServices.getSettingsClient(this)
                .checkLocationSettings(locationRequestBuilder.build())
                .addOnCompleteListener { task ->
                    try { // Location settings are On
                        task.getResult(ApiException::class.java)
                        networkLocationServiceListener(LocationResponse.ENABLED)
                    } catch (exception: ApiException) {
                        when (exception.statusCode) {
                            // Location settings are Off. Check result in onActivityResult()
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val resolvable = exception as ResolvableApiException
                                resolvable.startResolutionForResult(this, locationRequest)
                            } catch (ignored: IntentSender.SendIntentException) {
                            } catch (ignored: ClassCastException) {
                            } // Location settings not available on device
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                                networkLocationServiceListener(LocationResponse.UNAVAILABLE)
                        }
                    }
                }
    }

    // toast message extension to be used only in activity scope with String Resources
    protected fun Context.toast(@StringRes msg: Int, length: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(this, this.resources.getText(msg), length).show()

    enum class LocationResponse {
        NO_NETWORK, ENABLED, DISABLED, UNAVAILABLE
    }
}