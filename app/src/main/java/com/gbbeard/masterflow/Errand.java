package com.gbbeard.masterflow;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Errand {
    String id;
    String description;
    LatLng start;
    LatLng end;
    boolean isComplete;

    public Errand () {}

    public Errand (String description, LatLng start, LatLng end) {
        this.id = "";
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public static Errand fromSnapshot(DataSnapshot snapshot) {
        Errand errand = new Errand();
        errand.id = snapshot.getKey();
        errand.description = snapshot.child("description").getValue(String.class);
        errand.isComplete = snapshot.child("isComplete").getValue(boolean.class);

        float startLatitude = snapshot.child("start").child("lat").getValue(float.class);
        float startLongitude = snapshot.child("start").child("long").getValue(float.class);
        errand.start = new LatLng(startLatitude, startLongitude);

        float endLatitude = snapshot.child("end").child("lat").getValue(float.class);
        float endLongitude = snapshot.child("end").child("long").getValue(float.class);
        errand.end = new LatLng(endLatitude, endLongitude);

        return errand;
    }

}