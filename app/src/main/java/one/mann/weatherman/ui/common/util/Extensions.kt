package one.mann.weatherman.ui.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

/** Load vector resources directly for improved performance. Uses nightIcons after sunset, dayIcons used by default */
internal fun ImageView.loadIcon(iconCode: Int, sunPosition: Float = 1f) {
    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
    setImageResource(context.resources.getIdentifier(uri, "drawable", context.packageName))
}

/** Get file name for vector resource */
internal fun getUri(iconCode: Int, sunPosition: Float): String =
        if (sunPosition in 0.0..1.0) dayIcons(iconCode)
        else nightIcons(iconCode)

/** Inflate ViewGroups with ViewHolders */
internal fun ViewGroup.inflateView(@LayoutRes resource: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context)
        .inflate(resource, this, attachToRoot)

/** Check status of network connection */
internal fun Context.checkNetworkConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

/** Instantiate ViewModel */
internal inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory): VM =
        ViewModelProviders.of(this, factory)[VM::class.java]

///** Load resources using GlideApp */
//internal fun ImageView.loadIcon(iconCode: Int, sunPosition: Float = 1f) {
//    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
//    GlideApp.with(context)
//            .load(context.resources.getIdentifier(uri, "drawable", context.packageName))
//            .skipMemoryCache(true) // Skipped to ensure image is updated after every refresh
//            .into(this)
//}