package com.habosa.saf.demo;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.habosa.saf.Screen;
import com.habosa.saf.ScreenState;

import pub.devrel.bundler.BundlerClass;

/**
 * Screen that hosts a Fragment. Note that this is discouraged (SAF tries to take away the pain
 * of using Fragments) but it is possible when necessary by making assumptions about the screen
 * Host.
 *
 * TODO: Remove API key from being hardcoded in AndroidManifest.xml
 */
public class MapScreen extends Screen<MapScreen.MarkerState> implements OnMapReadyCallback {

    private MarkerState mState;
    private Marker mMarker;

    private FragmentManager mFragmentManager;
    private SupportMapFragment mMapFragment;

    @Override
    public int getLayout() {
        return R.layout.screen_map;
    }

    @Override
    public void onDisplay(View view, MarkerState state) {
        // Check for proper host
        if (!(getHost() instanceof FragmentActivity)) {
            throw new IllegalStateException("MapScreen must be hosted by a FragmentActivity");
        }

        mState = state;

        // Get Fragment Manager from the host
        mFragmentManager = ((FragmentActivity) getHost()).getSupportFragmentManager();

        // Initialize map Fragment
        mMapFragment = (SupportMapFragment) mFragmentManager.findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onHide() {
        // Called when screen is hidden, free up fragment resources.
        // We need to use commitAllowingStateLoss() here because onHidden is often called
        // after onSaveInstanceState.
        mFragmentManager
                .beginTransaction()
                .remove(mMapFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public MarkerState onSaveState() {
        return mState;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Get the LatLng from the screen state
        LatLng position = mState.getLatLng();

        // Draw marker from state
        mMarker = googleMap.addMarker(new MarkerOptions().position(position));

        // When the map is clicked, move the market
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Display the marker
                mMarker.setPosition(latLng);

                // Save marker position in state
                mState = new MarkerState(latLng);
            }
        });
    }

    /**
     * State that tracks a single point on the map.
     */
    @BundlerClass
    public static class MarkerState extends ScreenState {

        public double lat;
        public double lng;

        public MarkerState() {}

        public MarkerState(LatLng latLng) {
            this.lat = latLng.latitude;
            this.lng = latLng.longitude;
        }

        public LatLng getLatLng() {
            return new LatLng(lat, lng);
        }
    }
}
