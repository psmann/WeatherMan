package one.mann.weatherman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import one.mann.weatherman.api.OpenWeatherMapApi;
import one.mann.weatherman.model.Main;
import one.mann.weatherman.model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView currentTemp, maxTemp, minTemp, humidity, pressure, geoLocation;
    private final Double[] geoCoordinates = new Double[2];
    private final int LOCATION_REQUEST_CODE = 1011;
    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTemp = findViewById(R.id.current_temp);
        maxTemp = findViewById(R.id.max_temp);
        minTemp = findViewById(R.id.min_temp);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        geoLocation = findViewById(R.id.geo_location);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // this gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER (itself)
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
                Toast.makeText(this, "Permission required for location detection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE)
            switch (resultCode) {
                case MainActivity.RESULT_OK:
                    getGeoLocation();
                    break;
                case MainActivity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS needed for precise location detection.", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    @SuppressLint("MissingPermission") // locationProviderClient is being checked for permissions before this method is called
    private void getGeoLocation() {
        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null)
                    for (Location location : locationResult.getLocations()) {
                        geoCoordinates[0] = location.getLatitude();
                        geoCoordinates[1] = location.getLongitude();
                        checkWeather();
                        locationProviderClient.removeLocationUpdates(this);
                    }
            }
        };
        locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    geoCoordinates[0] = location.getLatitude();
                    geoCoordinates[1] = location.getLongitude();
                    checkWeather();
                } else
                    locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        });
    }

    private void checkWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
        Call<Weather> weatherCall = openWeatherMapApi.getWeather(geoCoordinates[0], geoCoordinates[1], "metric",
                "bd7173aa3aec6c2d8f88b500666a116e");

        geoLocation.append(" " + geoCoordinates[0] + ", " + geoCoordinates[1]);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "404", Toast.LENGTH_SHORT).show();
                    return;
                }
                Weather weather = response.body();
                if (weather == null)
                    return;
                Main main = weather.getMain();
                currentTemp.append(" " + main.getTemp());
                maxTemp.append(" " + main.getTemp_max());
                minTemp.append(" " + main.getTemp_min());
                humidity.append(" " + main.getHumidity());
                pressure.append(" " + main.getPressure());
            }

            @Override
            public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                currentTemp.setText(t.getMessage());
            }
        });
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try { // Location settings are on
                    task.getResult(ApiException.class);
                    getGeoLocation();
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
            }
        });
    }
}