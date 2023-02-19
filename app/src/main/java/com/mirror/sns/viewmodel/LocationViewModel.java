package com.mirror.sns.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.model.UserLocation;
import com.mirror.sns.repository.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository repository;
    private LiveData<UserLocation> location;

    private LiveData<List<String>> nearUsersUid;

    public LocationViewModel(Application application) {
        super(application);
        repository = new LocationRepository(application);
        location = repository.getLocation();
        nearUsersUid = repository.getNearUsersUid();
    }

    public LiveData<UserLocation> getLocation() { return location; }

    public LiveData<List<String>> getNearUsersUid() { return nearUsersUid; }

    public void getLocation(String uid) { repository.getLocation(uid);}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setLocation(String uid) { repository.setLocation(uid);}

    public void getNearUsersUid(UserLocation userLocation) { repository.getNearUsersUid(userLocation); }
}
