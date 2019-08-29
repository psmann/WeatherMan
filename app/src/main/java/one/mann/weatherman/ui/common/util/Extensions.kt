package one.mann.weatherman.ui.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

/** Load vector resources directly for improved performance. Uses nightIcons after sunset, dayIcons used by default */
internal fun ImageView.loadIcon(iconCode: Int, sunPosition: Float = 1f) {
    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
    setImageResource(context.resources.getIdentifier(uri, "drawable", context.packageName))
}

/** Inflate ViewGroups with ViewHolders */
internal fun ViewGroup.inflateView(@LayoutRes resource: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context)
        .inflate(resource, this, attachToRoot)

/** Check status of network connection, deprecation being handled */
@Suppress("DEPRECATION")
internal fun Context.isConnected(): Boolean = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        .run {
            if (VERSION.SDK_INT >= VERSION_CODES.M) getNetworkCapabilities(activeNetwork).run {
                return when {
                    this == null -> false
                    hasTransport(TRANSPORT_WIFI) || hasTransport(TRANSPORT_CELLULAR) || hasTransport(TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
            else activeNetworkInfo.run { return this?.isConnected ?: false }
        }

/** Instantiate ViewModel */
internal inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory): VM =
        ViewModelProvider(this, factory)[VM::class.java]

///** Load resources using GlideApp */ // Not being used, images are loaded directly for improved performance
//internal fun ImageView.loadIcon(iconCode: Int, sunPosition: Float = 1f) {
//    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
//    GlideApp.with(context)
//            .load(context.resources.getIdentifier(uri, "drawable", context.packageName))
//            .skipMemoryCache(true) // Skipped to ensure image is updated after every refresh
//            .into(this)
//}