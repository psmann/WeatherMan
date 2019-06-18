package one.mann.weatherman.ui.common

import android.content.Context
import android.net.ConnectivityManager
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

// Check status of network connection
internal fun Context.checkNetworkConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

internal fun isOnline() {
    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
//    public boolean isOnline() {
//        try {
//            int timeoutMs = 1500;
//            Socket sock = new Socket();
//            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
//
//            sock.connect(sockaddr, timeoutMs);
//            sock.close();
//
//            return true;
//        } catch (IOException e) { return false; }
//    }
}