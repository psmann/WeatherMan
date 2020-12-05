package one.mann.weatherman.ui.common.util

import android.content.Context
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
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

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

/** Show slide-up animation */
internal fun View.animateSlideUp(animationDuration: Long = 750L) {
    startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.anim_slide_up).apply {
                interpolator = FastOutSlowInInterpolator()
                duration = animationDuration
            }
    )
}