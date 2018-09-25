package assignment.codefellows.master_detail_map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback{

    private static final int REQUEST_PERMISSION_GRANT = 1;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng mCurrentLocation;
    private final int LOCATION_REFRESH_TIME = 1;
    private final int LOCATION_REFRESH_DISTANCE = 1;

    private String key;
    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.activity_maps,container,false);

        ButterKnife.bind(this,root);
        if(getArguments()!=null&&getArguments().containsKey("id")){
            key = getArguments().getString("id");
        }else if(getActivity().getIntent() != null){
            key = getActivity().getIntent().getStringExtra("id");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getGPSPermissions();
        return root;
    }

    private void attachDBListener(){
        final Intent data = getActivity().getIntent();

        FirebaseDatabase.getInstance().getReference("tasks")
                .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task task = Task.fromSnapshot(dataSnapshot);

                mMap.addMarker(
                        new MarkerOptions().title("start").position(task.start)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                );
                mMap.addMarker(
                        new MarkerOptions().title("end").position(task.end)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
                int padding = 200;
                LatLngBounds bounds = new LatLngBounds(task.end,task.start);
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,padding));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getGPSPermissions(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            initializeLocationListener();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_PERMISSION_GRANT);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationListener(){
        LocationListener listener = this;
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME,LOCATION_REFRESH_TIME,listener);
    }

    @Override
    public void onRequestPermissionsResult(int resultCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(resultCode == REQUEST_PERMISSION_GRANT && grantResults[0] == RESULT_OK &&
                resultCode == REQUEST_PERMISSION_GRANT && grantResults[1] == RESULT_OK) {
            initializeLocationListener();
        }
    }

    @Override
    public void onMapReady(GoogleMap gmap){
        mMap = gmap;
        attachDBListener();
    }

    @Override
    public void onLocationChanged(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrentLocation = latLng;
    }

    @OnClick(R.id.goToMyLocation)
    public void findMe(){
        if(mCurrentLocation != null){
            int padding = 200;
            LatLngBounds bounds = LatLngBounds.builder().include(mCurrentLocation).build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,padding));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle){}

    @Override
    public void onProviderEnabled(String s){}

    @Override
    public void onProviderDisabled(String s){}
}
