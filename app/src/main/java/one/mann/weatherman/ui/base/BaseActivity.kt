package one.mann.weatherman.ui.base

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import one.mann.weatherman.R

internal abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val locationRequest = 1011
    }

    protected var locationPermissionListener: () -> Unit = {}


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequest)
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                locationPermissionListener()
            } else {
                toast(R.string.permission_required)
                finish()
            }
    }

    // Gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION)
    protected fun getCheckLocationPermission(): Boolean =
            if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), locationRequest)
                false
            } else true

    protected fun Context.toast(@StringRes msg: Int, length: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(this, this.resources.getText(msg), length).show()
}