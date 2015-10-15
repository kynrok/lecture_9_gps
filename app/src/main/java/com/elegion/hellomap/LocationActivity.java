package com.elegion.hellomap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.sample.geofencing.GeofenceTransitionsIntentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Alex
 * Date creation: 22.02.15.
 */
public class LocationActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, ResultCallback<Status> {

    // TODO: Step 1 - manifest

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            lastLocation.getLatitude();
            lastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not to connect.
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(500);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        // Used to stop location updates.
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        // A new location.
    }

    /**
     * Start Geofencing here.
     */
    private void geofencing() {
        // start
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

        // stop
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().
    }


    private GeofencingRequest getGeofencingRequest() {
        List<Geofence> geofences = new ArrayList<>();
        geofences.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("REQUEST_ID")
                .setCircularRegion(
                        0.0,
                        0.0,
                        500
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
//        if (mGeofencePendingIntent != null) {
//            return mGeofencePendingIntent;
//        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {
        // TODO:!
    }
}
