package one.mann.weatherman.ui.common.base

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED
import com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE
import one.mann.domain.models.location.LocationResponse
import one.mann.domain.models.location.LocationResponse.*
import one.mann.weatherman.ui.common.util.isConnected

/* Created by Psmann. */

/** Base activity for all activities that need location services */
internal abstract class BaseLocationActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST_CODE = 1011
        private var locationPermissionListener: (Boolean) -> Unit = {}
        private var networkAndLocationListener: (LocationResponse) -> Unit = {}
        private val locationRequestBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS) // Make navigation bar and status bar transparent
        injectDependencies()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) locationPermissionListener(true)
            else locationPermissionListener(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE) when (resultCode) {
            Activity.RESULT_OK -> networkAndLocationListener(ENABLED)
            Activity.RESULT_CANCELED -> networkAndLocationListener(DISABLED)
        }
    }

    /** Field injection for Dagger components*/
    protected abstract fun injectDependencies()

    /** Give permissions for NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION) */
    protected fun handleLocationPermission(result: (Boolean) -> Unit) {
        locationPermissionListener = result
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else locationPermissionListener(true)
    }

    /** Check status of location services and handle in lambda */
    protected fun checkLocationService(prompt: Boolean = false, result: (LocationResponse) -> Unit) {
        if (!isConnected()) {
            result(NO_NETWORK)
            return
        }
        networkAndLocationListener = result
        LocationServices.getSettingsClient(this)
                .checkLocationSettings(locationRequestBuilder.build())
                .addOnCompleteListener {
                    try { // Location settings are On
                        it.getResult(ApiException::class.java)
                        networkAndLocationListener(ENABLED)
                    } catch (exception: ApiException) { // Location settings are Off
                        if (prompt) when (exception.statusCode) {
                            RESOLUTION_REQUIRED -> try { // Check result in onActivityResult
                                val resolvable = exception as ResolvableApiException
                                resolvable.startResolutionForResult(this, LOCATION_REQUEST_CODE)
                            } catch (ignored: IntentSender.SendIntentException) {
                            } catch (ignored: ClassCastException) {
                            } // Location settings are not available on device
                            SETTINGS_CHANGE_UNAVAILABLE -> networkAndLocationListener(UNAVAILABLE)
                        } else networkAndLocationListener(DISABLED)
                    }
                }
    }

    /** Toast extension function to be used only in activity scope with String Resources */
    protected fun Context.toast(@StringRes msg: Int, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, this.resources.getText(msg), length).show()
    }
}