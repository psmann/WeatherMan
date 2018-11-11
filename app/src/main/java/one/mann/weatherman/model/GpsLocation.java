package one.mann.weatherman.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class GpsLocation {

    private LocationCallback locationCallback;
    private FusedLocationProviderClient locationProviderClient;
    private Double[] currentLocation;
    private GeoCoordinates geoCoordinates;

    public GpsLocation(Context context, GeoCoordinates geoCoordinates) {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        currentLocation = new Double[2];
        this.geoCoordinates = geoCoordinates;
    }

    @SuppressLint("MissingPermission") // locationProviderClient is being checked for permissions before this method is called
    public void getLocation() {
        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null)
                    for (Location location : locationResult.getLocations()) {
                        currentLocation[0] = location.getLatitude();
                        currentLocation[1] = location.getLongitude();
                        locationProviderClient.removeLocationUpdates(this);
                        geoCoordinates.getCoordinates(currentLocation);
                    }
            }
        };
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> { // first check for last location if available
            if (location != null) {
                currentLocation[0] = location.getLatitude();
                currentLocation[1] = location.getLongitude();
                geoCoordinates.getCoordinates(currentLocation);
            } else // otherwise request for a new location (drains battery)
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        });
    }

    public interface GeoCoordinates {
        void getCoordinates(Double[] location);
    }
}