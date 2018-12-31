package one.mann.weatherman.view

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.activity_main.*

import one.mann.weatherman.R
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.viewmodel.CurrentWeatherViewModel

class MainActivity : AppCompatActivity() {

    private val locationRequestCode = 1011
    private var weatherViewModel: CurrentWeatherViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weather_layout.visibility = View.INVISIBLE

        // Gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION)
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) // Check result in onRequestPermissionsResult()
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        locationRequestCode)
        } else
            initObjects()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initObjects()
            } else {
                finish()
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == locationRequestCode)
            when (resultCode) {
                Activity.RESULT_OK -> weatherViewModel!!.getWeather(true)
                Activity.RESULT_CANCELED -> weatherViewModel!!.getWeather(false)
            }
    }

    private fun initObjects() {
        weatherViewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        weatherViewModel!!.weatherLiveData.observe(this, Observer { weatherData ->
            current_temp_result.text = weatherData!!.getWeatherData(WeatherData.CURRENT_TEMP)
            feels_like_result.text = weatherData.getWeatherData(WeatherData.FEELS_LIKE)
            max_temp_result.text = weatherData.getWeatherData(WeatherData.MAX_TEMP)
            min_temp_result.text = weatherData.getWeatherData(WeatherData.MIN_TEMP)
            pressure_result.text = weatherData.getWeatherData(WeatherData.PRESSURE)
            humidity_result.text = weatherData.getWeatherData(WeatherData.HUMIDITY)
            location_result.text = weatherData.getWeatherData(WeatherData.LOCATION)
            last_checked_result.text = weatherData.getWeatherData(WeatherData.LAST_CHECKED)
            last_updated_result.text = weatherData.getWeatherData(WeatherData.LAST_UPDATED)
            city_name.text = weatherData.getWeatherData(WeatherData.CITY_NAME)
            country_flag.text = weatherData.getWeatherData(WeatherData.COUNTRY_FLAG)
            sunrise_result.text = weatherData.getWeatherData(WeatherData.SUNRISE)
            sunset_result.text = weatherData.getWeatherData(WeatherData.SUNSET)
            day_length_result.text = weatherData.getWeatherData(WeatherData.DAY_LENGTH)
            clouds_result.text = weatherData.getWeatherData(WeatherData.CLOUDS)
            wind_speed_result.text = weatherData.getWeatherData(WeatherData.WIND_SPEED)
            wind_direction_result.text = weatherData.getWeatherData(WeatherData.WIND_DIRECTION)
            visibility_result.text = weatherData.getWeatherData(WeatherData.VISIBILITY)
            description.text = weatherData.getWeatherData(WeatherData.DESCRIPTION)
            GlideApp.with(this@MainActivity)
                    .load(weatherData.getWeatherData(WeatherData.ICON_CODE))
                    .skipMemoryCache(true)
                    .into(weather_icon)
        })
        weatherViewModel!!.displayLoadingBar.observe(this, Observer { result ->
            swipe_refresh_layout.isRefreshing = result ?: false
        })
        weatherViewModel!!.displayUi.observe(this, Observer { aBoolean ->
            if (!aBoolean!!)
                checkLocationSettings()
            else if (weather_layout.visibility == View.INVISIBLE)
                weather_layout.visibility = View.VISIBLE
        })
        swipe_refresh_layout.setColorSchemeColors(Color.RED, Color.BLUE)
        swipe_refresh_layout.setOnRefreshListener { this.checkLocationSettings() }
    }

    private fun checkNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun checkLocationSettings() {
        if (!checkNetworkConnection()) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
            swipe_refresh_layout.isRefreshing = false
            return
        }
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval((10 * 1000).toLong())
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        val result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try { // Location settings are on
                task.getResult(ApiException::class.java)
                weatherViewModel!!.getWeather(true)
            } catch (exception: ApiException) {
                when (exception.statusCode) { // Settings are off. Show a prompt and check result in onActivityResult()
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this@MainActivity, locationRequestCode)
                    } catch (ignored: IntentSender.SendIntentException) {
                    } catch (ignored: ClassCastException) {
                    } // Location settings not available on the device, exit app
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Toast.makeText(this, R.string.location_settings_not_available, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}