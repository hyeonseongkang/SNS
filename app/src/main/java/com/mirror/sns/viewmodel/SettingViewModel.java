package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.repository.SettingRepository;

import org.jetbrains.annotations.NotNull;

public class SettingViewModel extends AndroidViewModel {

    private SettingRepository repository;

    private LiveData<Boolean> setRadiusResult;

    private LiveData<String> radiusLiveData;

    public SettingViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new SettingRepository(application);

        setRadiusResult = repository.getSetRadiusResult();
        radiusLiveData = repository.getRadiusLiveData();

    }

    public LiveData<Boolean> getSetRadiusResult() {
        return repository.getSetRadiusResult();
    };

    public LiveData<String> getRadiusLiveData() { return repository.getRadiusLiveData();}

    public void setRadius(String radius) {
        repository.setRadius(radius);
    }

    public void getRadius() {
        repository.getRadius();
    }
}
