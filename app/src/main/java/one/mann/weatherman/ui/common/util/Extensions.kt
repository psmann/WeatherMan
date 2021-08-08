package one.mann.weatherman.ui.common.util

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import one.mann.domain.models.Direction
import one.mann.domain.models.Direction.LEFT
import one.mann.domain.models.Direction.UP
import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons
import kotlin.coroutines.resume

/* Created by Psmann. */

/** Load vector resources directly for improved performance. Uses nightIcons after sunset, dayIcons used by default */
internal fun ImageView.loadIcon(iconCode: Int, sunPosition: Float = 1f) {
    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
    setImageResource(context.resources.getIdentifier(uri, "drawable", context.packageName))
}

/** Inflate ViewGroups with ViewHolders */
internal fun ViewGroup.inflateView(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}

/** Check status of network connection, deprecation being handled */
@Suppress("DEPRECATION") // activeNetworkInfo is deprecated in API 29 and its only being called below API 23
internal fun Context.isConnected(): Boolean {
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        if (VERSION.SDK_INT >= VERSION_CODES.M) getNetworkCapabilities(activeNetwork).run {
            return when {
                this == null -> false
                hasTransport(TRANSPORT_WIFI) || hasTransport(TRANSPORT_CELLULAR) || hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else activeNetworkInfo.run { return this?.isConnected ?: false }
    }
}

/** Instantiate ViewModel */
internal inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory): VM {
    return ViewModelProvider(this, factory)[VM::class.java]
}

/** Set sliding animation on a view */
internal fun View.setSlideAnimation(direction: Direction, animationDuration: Long = 750L) {
    startAnimation(
        AnimationUtils.loadAnimation(
            context,
            when (direction) {
                UP -> R.anim.anim_slide_up
                LEFT -> R.anim.anim_slide_left
            }
        ).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = animationDuration
        }
    )
}

/** Check if location permission has been granted and location services are enabled or not */
internal suspend fun Context.isLocationEnabled(): Boolean = suspendCancellableCoroutine { continuation ->
    val locationRequestBuilder = LocationSettingsRequest.Builder()
        .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))

    if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationRequestBuilder.build())
            .addOnCompleteListener {
                try {
                    // Location settings are On, return true
                    it.getResult(ApiException::class.java)
                    continuation.resume(true)
                } catch (exception: ApiException) {
                    // Location settings are Off, return false
                    continuation.resume(false)
                }
            }
    } else {
        // Location Permission not granted, return False
        continuation.resume(false)
    }
}

///** Set correct StatusBar and NavigationBar heights */
//internal fun View.adjustLayoutHeight() {
//    val density = resources.displayMetrics.density.toInt()
//    val statusBarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
//    val navBarId = resources.getIdentifier("status_bar_height", "dimen", "android")
//
//    this.apply {
//        val params = layoutParams as CoordinatorLayout.LayoutParams
//        params.topMargin = if (statusBarId > 0) resources.getDimensionPixelSize(statusBarId) else 48 * density
//        params.bottomMargin = when (resources.configuration.orientation) {
//            Configuration.ORIENTATION_PORTRAIT -> if (navBarId > 0) resources.getDimensionPixelSize(navBarId) else 0
//            else -> 0
//        }
//        layoutParams = params
//    }
//}