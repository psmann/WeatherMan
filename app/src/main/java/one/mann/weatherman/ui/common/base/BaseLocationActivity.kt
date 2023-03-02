package one.mann.weatherman.ui.common.base

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED
import com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE
import com.google.android.gms.location.Priority
import one.mann.domain.models.location.LocationServicesResponse
import one.mann.domain.models.location.LocationServicesResponse.*
import one.mann.weatherman.ui.common.util.isConnected

/* Created by Psmann. */

/** Base activity for all activities that need location services */
internal abstract class BaseLocationActivity : AppCompatActivity() {

    companion object {
        private var locationPermissionListener: (Boolean) -> Unit = {} // Delegate function object to activity callback
        private var networkAndLocationListener: (LocationServicesResponse) -> Unit = {}
        private val locationRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10 * 1000L).build())
    }

    private val requestPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        locationPermissionListener(it)
    }
    private val resolutionResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        when (it.resultCode) {
            RESULT_OK -> networkAndLocationListener(ENABLED)
            RESULT_CANCELED -> networkAndLocationListener(DISABLED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make navigation bar and status bar transparent
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        injectDependencies()
    }

    /** Field injection for Dagger components*/
    protected abstract fun injectDependencies()

    /** Give permissions for NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION) */
    protected fun handleLocationPermission(result: (Boolean) -> Unit) {
        locationPermissionListener = result
        requestPermissionResult.launch(ACCESS_FINE_LOCATION)
    }

    /** Check status of location services and handle in lambda */
    protected fun checkLocationService(prompt: Boolean = false, result: (LocationServicesResponse) -> Unit) {
        if (!isConnected()) {
            result(NO_NETWORK)
            return
        }
        networkAndLocationListener = result
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationRequestBuilder.build())
            .addOnCompleteListener {
                // If location settings are On
                try {
                    it.getResult(ApiException::class.java)
                    networkAndLocationListener(ENABLED)
                } catch (exception: ApiException) {
                    // If location settings are Off
                    if (prompt) when (exception.statusCode) {
                        // Prompt to enable location settings and check result in resolutionResult
                        RESOLUTION_REQUIRED -> try {
                            resolutionResult.launch(
                                IntentSenderRequest.Builder((exception as ResolvableApiException).resolution).build()
                            )
                        } catch (ignored: IntentSender.SendIntentException) {
                        } catch (ignored: ClassCastException) {
                        } // Location settings are not available on device
                        SETTINGS_CHANGE_UNAVAILABLE -> networkAndLocationListener(UNAVAILABLE)
                    } else networkAndLocationListener(DISABLED)
                }
            }
    }

    /** Toast extension function to be used only in activity scope with String Resources and an optional string */
    protected fun Context.toast(@StringRes msg: Int, length: Int = Toast.LENGTH_SHORT, errorMessage: String = "") {
        Toast.makeText(this, "${this.resources.getText(msg)}$errorMessage", length).show()
    }
}