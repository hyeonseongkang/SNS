package com.mirror.sns.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.mirror.sns.model.UserLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    public static final String TAG = "LocationRepository";

    private Application application;

    private DatabaseReference userLocationsRef;

    private MutableLiveData<UserLocation> location;

    private MutableLiveData<List<String>> nearUsersUid;

    public LocationRepository(Application application) {
        this.application = application;
        userLocationsRef = FirebaseDatabase.getInstance().getReference("userLocations");

        location = new MutableLiveData<>();
        nearUsersUid = new MutableLiveData<>();
    }

    public MutableLiveData<UserLocation> getLocation() { return location; }

    public MutableLiveData<List<String>> getNearUsersUid() {return nearUsersUid; }

    public void getLocation(String uid) {
        userLocationsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserLocation userLocationData = snapshot.getValue(UserLocation.class);
                location.setValue(userLocationData);
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

            userLocationsRef.child(uid).setValue(new UserLocation(uid, latitude, longitude));

        }
    }

    public void getNearUsersUid(UserLocation userLocation) {
        userLocationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<String> usersUid = new ArrayList<>();
                Location location1 = new Location("");
                Location location2 = new Location("");
                location1.setLatitude(userLocation.getLatitude());
                location1.setLongitude(userLocation.getLongitude());

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    UserLocation localUserLocation = snapshot1.getValue(UserLocation.class);
                    location2.setLatitude(localUserLocation.getLatitude());
                    location2.setLongitude(localUserLocation.getLongitude());

                    float distance = location1.distanceTo(location2);
                    float distance1 = location1.distanceTo(location2) / 1000; // km

                    // 2km 이내에 있는 유저 uid만 저장
                    if (distance1 < 2) {
                        usersUid.add(localUserLocation.getUid());
                    }
                }

                nearUsersUid.setValue(usersUid);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}
