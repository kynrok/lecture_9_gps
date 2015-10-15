package com.elegion.hellomap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Random;

/**
 * @author Alex
 */
public class ClusterActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ClusterManager<MarkerItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_cluster);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: URL - https://github.com/googlemaps/android-maps-utils/tree/master/library/src/com/google/maps/android/clustering
        mClusterManager = new ClusterManager<>(this, googleMap);
        mClusterManager.setAlgorithm(new GridBasedAlgorithm<MarkerItem>());
        mClusterManager.setRenderer(new DefaultClusterRenderer<MarkerItem>(this, googleMap, mClusterManager) {

            @Override
            protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
                markerOptions.title("Position " + item.getIndex());
                markerOptions.snippet("some snippet");

                super.onBeforeClusterItemRendered(item, markerOptions);
            }
        });

        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // helper to add random markers.
        addRandomLocations(googleMap);
    }

    private void addRandomLocations(GoogleMap googleMap) {
        Random random = new Random();
        LatLng location = googleMap.getCameraPosition().target;

        // Convert radius from meters to degrees
        double radiusInDegrees = 50000 / 111000f;

        for (int index = 0; index < 100; index++) {
            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double newX = x / Math.cos(location.latitude);

//            googleMap.addMarker(
//                    new MarkerOptions()
//                            .position(new LatLng(y + location.latitude, newX + location.longitude))
//                            .title("Item " + index + 1)
//            );
            mClusterManager.addItem(new MarkerItem(index, y + location.latitude, newX + location.longitude));
        }
    }

    public static class MarkerItem implements ClusterItem {

        private LatLng mPosition;

        private int mIndex;

        public MarkerItem(int index, double latitude, double longitude) {
            mIndex = index;
            mPosition = new LatLng(latitude, longitude);
        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }
}

