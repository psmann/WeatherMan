package one.mann.weatherman.ui.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import one.mann.weatherman.ui.common.GlideApp

// Set ImageView using GlideApp
internal fun ImageView.loadImage(url: String) = GlideApp.with(context)
        .load(url)
        .skipMemoryCache(true)
        .into(this)

// Inflate ViewGroups with ViewHolders
internal fun ViewGroup.inflateView(@LayoutRes resource: Int, attachToRoot: Boolean = false) =
        LayoutInflater.from(context)
                .inflate(resource, this, attachToRoot)

// Check status of network connection
internal fun Context.checkNetworkConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

// Instantiate ViewModel
internal inline fun <reified VM : ViewModel> Any.getViewModel(): VM = when (this) {
    is AppCompatActivity -> ViewModelProviders.of(this).get(VM::class.java)
    is Fragment -> ViewModelProviders.of(this).get(VM::class.java)
    else -> throw Exception()
}

// Instantiate ViewModel with constructor arguments; Cast is checked at call
@Suppress("UNCHECKED_CAST")
internal inline fun <reified VM : ViewModel> Any.getViewModel(crossinline factory: () -> VM): VM {
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <F : ViewModel?> create(modelClass: Class<F>): F = factory() as F
    }
    return when (this) {
        is AppCompatActivity -> ViewModelProviders.of(this, vmFactory).get(VM::class.java)
        is Fragment -> ViewModelProviders.of(this, vmFactory).get(VM::class.java)
        else -> throw Exception()
    }
}

// Check if internet is actually working
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