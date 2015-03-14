package com.sherifmakhlouf.goeuro.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class GooglePlayHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public interface LastLocationListener {
        /**
         * Will be called when a lastLocation is ready
         *
         * @param latitude
         * @param longitude
         */
        public void onLocationReady(double latitude, double longitude);
    }

    private final LastLocationListener listener;
    private  GoogleApiClient mGoogleApiClient;

    public GooglePlayHandler(LastLocationListener listener,Context context) {
        this.listener = listener;
        buildGoogleApiClient(context);
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
           listener.onLocationReady(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {


    }


    public void connect(){
        mGoogleApiClient.connect();
    }

    public void disconnect(){
        mGoogleApiClient.disconnect();
    }

    private synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

}
