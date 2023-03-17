package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.repository.SettingRepository;

import org.jetbrains.annotations.NotNull;

public class SettingViewModel extends AndroidViewModel {

    private SettingRepository repository;

    private LiveData<Boolean> SetRadiusResult;


    public SettingViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new SettingRepository(application);
    }

    public LiveData<Boolean> getSetRadiusResult() {
        return repository.getSetRadiusResult();
    };

    public void setRadius(String radius) {
        repository.setRadius(radius);
    }
}
