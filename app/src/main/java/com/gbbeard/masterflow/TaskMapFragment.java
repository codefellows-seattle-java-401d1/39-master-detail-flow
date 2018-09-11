package com.gbbeard.masterflow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class TaskMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    private static final int LOCATION_REFRESH_TIME = 1;
    private static final int LOCATION_REFRESH_DISTANCE = 1;
    private static final int REQUEST_PERMISSION_GRANT = 1;
    private static final String TAG = "";
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng mCurrentLocation;
    private String databaseKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_task_map, container, false);
        Intent data = getActivity().getIntent();
        Bundle arguments = getArguments();
        if (data != null &&  data.hasExtra("key")) {
            databaseKey = data.getStringExtra("key");
        } else if (arguments != null && arguments.containsKey("key")){
            databaseKey = arguments.getString("key");
        }
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkPermissions();
        return view;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeLocationListener();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_PERMISSION_GRANT );
        }
    }

    private void attachFirebaseListener() {
        if (databaseKey == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference("errand")
                .child(databaseKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Errand errand = Errand.fromSnapshot(dataSnapshot);
                mMap.addMarker(
                        new MarkerOptions().position(errand.start).title("start")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );
                mMap.addMarker(
                        new MarkerOptions().position(errand.end).title("end")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );

                LatLngBounds bounds = LatLngBounds.builder()
                        .include(errand.start)
                        .include(errand.end)
                        .build();

                int padding = 150;
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationListener() {
        LocationListener listener = this;
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GRANT && grantResults[0] == Activity.RESULT_OK && grantResults[1] == Activity.RESULT_OK) {
            initializeLocationListener();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng geiselwind = new LatLng(49.77, 10.47);
        mMap.addMarker(new MarkerOptions().position(geiselwind).title("Marker in Geiselwind"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(geiselwind));

        attachFirebaseListener();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLocation = latLng;
    }

    @OnClick(R.id.goToMyLocation)
    public void goToMyLocation () {
        if (mCurrentLocation != null) {
            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));

        }
    }

    @OnClick(R.id.goToErrandList)
    public void goToMyErrandList() {
        Intent intent = new Intent(getActivity(), ErrandListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.gpsOff)
    public void turnGpsOff() {
        onRequestPermissionsResult(0, new String[]{"off"}, new int[]{0, 0});
        Toast.makeText(getActivity().getApplicationContext(), "GPS is off", Toast.LENGTH_LONG).show();
        Log.d("GPS", "gps turned off");
    }

    @OnClick(R.id.gpsOn)
    public void turnGpsOn() {
        initializeLocationListener();
        Toast.makeText(getActivity().getApplicationContext(), "GPS is on", Toast.LENGTH_LONG).show();
        Log.d("GPS", "gps turned on");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("GPS", "gps turned on");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("GPS", "gps turned off");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LIFECYCLE", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LIFECYCLE", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "onDestroy");
    }
}