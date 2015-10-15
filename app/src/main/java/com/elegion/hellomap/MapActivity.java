package com.elegion.hellomap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Alex
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_map);

        // Step 5
        GoogleMapOptions options = new GoogleMapOptions();
        options.camera(
                CameraPosition
                        .builder()
                        .target(new LatLng(48.858093, 2.294694))
                        .zoom(18)
                        .bearing(40)
                        .build()
        ).compassEnabled(true)
//                .mapType(GoogleMap.MAP_TYPE_NONE)
                .mapType(GoogleMap.MAP_TYPE_HYBRID)
                .zoomControlsEnabled(true)
                .mapToolbarEnabled(false);
//                .liteMode(true);
        MapFragment mapFragment = MapFragment.newInstance(options);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.map_container, mapFragment)
                    .commit();
        }

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Step 6: work with map.
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Hello Map")
                                .snippet("Snippet")
                                .alpha(0.7f)
                                .draggable(true)
                );
            }
        });

//        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 3000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        // some action
                    }

                    @Override
                    public void onCancel() {
                        // some action
                    }
                });
                onMarkerCLicked(googleMap, marker);
//                return false;
                return true;
            }
        });

//        googleMap.setPadding(0, 300, 0, 0);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
            }
        });
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });

        // ground overlay example.
        googleMap.addGroundOverlay(
                new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
                        .positionFromBounds(new LatLngBounds(new LatLng(48.80, 2.25), new LatLng(48.90, 2.35)))
                        .transparency(0.7f)
        );

        // TODO: URL
        // some examples: https://developers.google.com/maps/documentation/android-api/
    }

    private void onMarkerCLicked(GoogleMap googleMap, Marker marker) {
        googleMap.addCircle(
                new CircleOptions()
                        .center(marker.getPosition())
                        .fillColor(Color.GREEN)
                        .radius(30)
        );
    }
}
