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
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

import one.mann.weatherman.R
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.viewmodel.CurrentWeatherViewModel

class MainActivity : AppCompatActivity() {

    private val locationRequestCode = 1011
    private var currentTemperature: TextView? = null
    private var maxTemperature: TextView? = null
    private var minTemperature: TextView? = null
    private var humidity: TextView? = null
    private var pressure: TextView? = null
    private var geoLocation: TextView? = null
    private var lastUpdated: TextView? = null
    private var cityName: TextView? = null
    private var lastChecked: TextView? = null
    private var sunrise: TextView? = null
    private var sunset: TextView? = null
    private var clouds: TextView? = null
    private var windSpeed: TextView? = null
    private var windDirection: TextView? = null
    private var visibility: TextView? = null
    private var description: TextView? = null
    private var countryflag: TextView? = null
    private var dayLength: TextView? = null
    private var weatherIcon: ImageView? = null
    private var weatherViewModel: CurrentWeatherViewModel? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var constraintLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constraintLayout = findViewById(R.id.weather_layout)
        constraintLayout!!.visibility = View.INVISIBLE

        // Gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (FINE_LOCATION)
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23)
            // Check result in onRequestPermissionsResult()
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
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        currentTemperature = findViewById(R.id.current_temp_result)
        maxTemperature = findViewById(R.id.max_temp_result)
        minTemperature = findViewById(R.id.min_temp_result)
        humidity = findViewById(R.id.humidity_result)
        pressure = findViewById(R.id.pressure_result)
        geoLocation = findViewById(R.id.location_result)
        lastChecked = findViewById(R.id.last_checked_result)
        lastUpdated = findViewById(R.id.last_updated_result)
        cityName = findViewById(R.id.city_name)
        countryflag = findViewById(R.id.country_flag)
        sunrise = findViewById(R.id.sunrise_result)
        sunset = findViewById(R.id.sunset_result)
        dayLength = findViewById(R.id.day_length_result)
        clouds = findViewById(R.id.clouds_result)
        windSpeed = findViewById(R.id.wind_speed_result)
        windDirection = findViewById(R.id.wind_direction_result)
        visibility = findViewById(R.id.visibility_result)
        description = findViewById(R.id.description)
        weatherIcon = findViewById(R.id.weather_icon)
        swipeRefreshLayout!!.setColorSchemeColors(Color.RED, Color.BLUE)

        weatherViewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        weatherViewModel!!.weatherLiveData.observe(this, Observer { weatherData ->
            //if (weatherData == null) return//@weatherViewModel.getWeatherLiveData().observe
            currentTemperature!!.text = weatherData!!.getWeatherData(WeatherData.CURRENT_TEMP)
            maxTemperature!!.text = weatherData.getWeatherData(WeatherData.MAX_TEMP)
            minTemperature!!.text = weatherData.getWeatherData(WeatherData.MIN_TEMP)
            pressure!!.text = weatherData.getWeatherData(WeatherData.PRESSURE)
            humidity!!.text = weatherData.getWeatherData(WeatherData.HUMIDITY)
            geoLocation!!.text = weatherData.getWeatherData(WeatherData.LOCATION)
            lastChecked!!.text = weatherData.getWeatherData(WeatherData.LAST_CHECKED)
            lastUpdated!!.text = weatherData.getWeatherData(WeatherData.LAST_UPDATED)
            cityName!!.text = weatherData.getWeatherData(WeatherData.CITY_NAME)
            countryflag!!.text = weatherData.getWeatherData(WeatherData.COUNTRY_FLAG)
            sunrise!!.text = weatherData.getWeatherData(WeatherData.SUNRISE)
            sunset!!.text = weatherData.getWeatherData(WeatherData.SUNSET)
            dayLength!!.text = weatherData.getWeatherData(WeatherData.DAY_LENGTH)
            clouds!!.text = weatherData.getWeatherData(WeatherData.CLOUDS)
            windSpeed!!.text = weatherData.getWeatherData(WeatherData.WIND_SPEED)
            windDirection!!.text = weatherData.getWeatherData(WeatherData.WIND_DIRECTION)
            visibility!!.text = weatherData.getWeatherData(WeatherData.VISIBILITY)
            description!!.text = weatherData.getWeatherData(WeatherData.DESCRIPTION)
            GlideApp.with(this@MainActivity)
                    .load(weatherData.getWeatherData(WeatherData.ICON_CODE))
                    .skipMemoryCache(true)
                    .into(weatherIcon!!)
        })
        weatherViewModel!!.displayLoadingBar.observe(this, Observer {
            result -> swipeRefreshLayout!!.isRefreshing = result ?: false })
        weatherViewModel!!.displayUi.observe(this, Observer { aBoolean ->
            //if (aBoolean == null) return//@weatherViewModel.getDisplayUi().observe
            if (!aBoolean!!)
                checkLocationSettings()
            else if (constraintLayout!!.visibility == View.INVISIBLE)
                constraintLayout!!.visibility = View.VISIBLE
        })
        swipeRefreshLayout!!.setOnRefreshListener { this.checkLocationSettings() }
    }

    private fun checkNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo// Suppressed because null is being checked
        return networkInfo != null && networkInfo.isConnected
    }

    private fun checkLocationSettings() {
        if (!checkNetworkConnection()) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
            swipeRefreshLayout!!.isRefreshing = false
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
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try { // Location settings are off. Show a prompt and check result in onActivityResult()
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this@MainActivity, locationRequestCode)
                    } catch (ignored: IntentSender.SendIntentException) {
                    } catch (ignored: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Toast.makeText(this, R.string.location_settings_not_available, Toast.LENGTH_SHORT)
                                .show()
                        finish() // Location settings not available on the device, exit app
                    }
                }
            }
        }
    }
}