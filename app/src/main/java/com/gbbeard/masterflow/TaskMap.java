package com.gbbeard.masterflow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TaskMap extends FragmentActivity implements OnMapReadyCallback, LocationListener  {

    private GoogleMap mMap;
    private static final int LOCATION_REFRESH_TIME = 1;
    private static final int LOCATION_REFRESH_DISTANCE = 1;
    private static final int REQUEST_PERMISSION_GRANT = 1;
    private static final String TAG = "";
    private LocationManager locationManager;
    private LatLng mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_map);

        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Intent data = getIntent();

        FirebaseDatabase.getInstance()
                .getReference("errands")
                .child(data.getStringExtra("id"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Errand errand = Errand.fromSnapshot(dataSnapshot);
                        mMap.addMarker(new MarkerOptions().position(errand.start).title("start"));
                        mMap.addMarker(new MarkerOptions().position(errand.end).title("end"));

                        double centerLat = (errand.start.latitude + errand.end.latitude) / 2;
                        double centerLng = (errand.start.longitude + errand.end.longitude) / 2;

                        mMap.moveCamera(CameraUpdateFactory.zoomTo(8));

                        LatLng center = new LatLng(centerLat, centerLng);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(center));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeLocationListener();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_PERMISSION_GRANT );
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationListener() {
        LocationListener listener = this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GRANT && grantResults[0] == RESULT_OK && grantResults[1] == RESULT_OK) {
            initializeLocationListener();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng geiselwind = new LatLng(49.77, 10.47);
        mMap.addMarker(new MarkerOptions().position(geiselwind).title("Marker in Geiselwind"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(geiselwind));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLocation = latLng;
    }

    @OnClick(R.id.goToMyLocation)
    public void goToMyLocation(){
        if(mCurrentLocation != null) {
            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("Location Centered"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));
        }
    }

    @OnClick(R.id.goToErrandList)
    public void goToMyErrandList() {
        Intent intent = new Intent(this, ErrandListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.gpsOff)
    public void gpsOff(){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        Toast.makeText(getApplicationContext(),"GPS is now off", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.gpsOn)
    public void gpsOn(){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        Toast.makeText(getApplicationContext(),"GPS is now on", Toast.LENGTH_SHORT).show();
        sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {
        Log.d("GPS", "gps turned on");
    }

    @Override
    public void onProviderDisabled(String s) {
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
    public void onRestart() {
        super.onRestart();
        Log.d("LIFECYCLE", "onRestart");
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