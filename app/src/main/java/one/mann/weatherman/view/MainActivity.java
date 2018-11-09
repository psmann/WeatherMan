package one.mann.weatherman.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import one.mann.weatherman.R;
import one.mann.weatherman.viewmodel.CurrentWeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private TextView currentTemp, maxTemp, minTemp, humidity, pressure, geoLocation;
    private final int LOCATION_REQUEST_CODE = 1011;
    private CurrentWeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTemp = findViewById(R.id.current_temp_result);
        maxTemp = findViewById(R.id.max_temp_result);
        minTemp = findViewById(R.id.min_temp_result);
        humidity = findViewById(R.id.humidity_result);
        pressure = findViewById(R.id.pressure_result);
        geoLocation = findViewById(R.id.geo_location_result);

        weatherViewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel.class);
        weatherViewModel.getCurrentTemperature().observe(this, s -> currentTemp.setText(s));
        weatherViewModel.getMaxTemperature().observe(this, s -> maxTemp.setText(s));
        weatherViewModel.getMinTemperature().observe(this, s -> minTemp.setText(s));
        weatherViewModel.getPressure().observe(this, s -> pressure.setText(s));
        weatherViewModel.getHumidity().observe(this, s -> humidity.setText(s));
        weatherViewModel.getLocation().observe(this, s -> geoLocation.setText(s));
        weatherViewModel.getDisplayProgressBar().observe(this, this::loadingProgressDialog);

        // gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) // check result in onRequestPermissionsResult()
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            return;
        }
        checkLocationSettings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                checkLocationSettings();
            else
                Toast.makeText(this, "Permission is required for location detection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE)
            switch (resultCode) {
                case MainActivity.RESULT_OK:
                    weatherViewModel.geoLocation();
                    break;
                case MainActivity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS is needed for current location detection.", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    private void loadingProgressDialog(boolean show) {
        ProgressBar progressBar = findViewById(R.id.loading_progressBar);
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setElevation(50);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            progressBar.setElevation(0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        @SuppressWarnings("ConstantConditions") // Suppressed because null is being checked
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void checkLocationSettings() {
        if(!checkNetworkConnection()) {
            Toast.makeText(this, "No active internet connection found.", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try { // Location settings are on
                task.getResult(ApiException.class);
                weatherViewModel.geoLocation();
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try { // Location settings are off. Show a prompt and check result in onActivityResult()
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(MainActivity.this, LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException | ClassCastException ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break; // Location settings are not available on the device
                }
            }
        });
    }
}