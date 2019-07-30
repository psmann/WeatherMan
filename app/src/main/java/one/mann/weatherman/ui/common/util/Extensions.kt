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
import one.mann.weatherman.ui.common.GlideApp

/** Set ImageView using GlideApp, use NightIcons after sunset. DayIcons used by default */
internal fun ImageView.loadImage(iconCode: Int, sunPosition: Float = 1f) {
    val uri = if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)
    GlideApp.with(context)
            .load(context.resources.getIdentifier(uri, "drawable", context.packageName))
            .skipMemoryCache(true) // Skipped to ensure image is updated after every refresh
            .into(this)
}

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

//internal suspend fun isOnline(): Boolean = try {
//    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP)
//    val timeoutMs = 1500
//    val socket = Socket()
//    val socketAddress = InetSocketAddress("8.8.8.8", 53) // Google DNS
//    socket.connect(socketAddress, timeoutMs)
//    socket.close()
//    true
//} catch (ignored: IOException) {
//    false
//}