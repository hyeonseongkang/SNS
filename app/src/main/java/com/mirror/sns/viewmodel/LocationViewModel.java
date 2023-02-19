package com.mirror.sns.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.sns.model.Location;
import com.mirror.sns.repository.LocationRepository;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository repository;
    private LiveData<Location> location;

    public LocationViewModel(Application application) {
        super(application);
        repository = new LocationRepository(application);
        location = repository.getLocation();
    }

    public LiveData<Location> getLocation() { return location; }

    public void getLocation(String uid) { repository.getLocation(uid);}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setLocation(String uid) { repository.setLocation(uid);}
}
