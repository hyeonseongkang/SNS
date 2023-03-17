package com.mirror.sns.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

public class SettingRepository {

    Application application;

    private MutableLiveData<Boolean> setRadiusResult;


    public SettingRepository(Application application) {
        this.application = application;

        setRadiusResult = new MutableLiveData<>();

    }

    public MutableLiveData<Boolean> getSetRadiusResult() {
        return setRadiusResult;
    }

    public void setRadius(String radius) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("setting", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("radius", radius);
        editor.commit();
        setRadiusResult.setValue(true);

    }
}
