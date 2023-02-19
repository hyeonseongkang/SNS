package com.mirror.sns.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.sns.model.Location;

import org.jetbrains.annotations.NotNull;

public class LocationRepository {
    public static final String TAG = "LocationRepository";

    private Application application;

    private DatabaseReference userLocationsRef;

    private MutableLiveData<Location> location;

    public LocationRepository(Application application) {
        this.application = application;
        userLocationsRef = FirebaseDatabase.getInstance().getReference("userLocations");

        location = new MutableLiveData<>();
    }

    public MutableLiveData<Location> getLocation() { return location; }

    public void getLocation(String uid) {
        userLocationsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Location locationData = snapshot.getValue(Location.class);
                location.setValue(locationData);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setLocation(String uid) {
        LocationManager locationManager = null;
        locationManager =  (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        application.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(application.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) application.getMainExecutor(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            userLocationsRef.child(uid).setValue(new Location(uid, latitude, longitude));

        }
    }


}
