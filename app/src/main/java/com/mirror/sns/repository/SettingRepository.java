package com.mirror.sns.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

public class SettingRepository {

    Application application;

    private MutableLiveData<Boolean> setRadiusResult;

    private MutableLiveData<String> radiusLiveData;


    public SettingRepository(Application application) {
        this.application = application;

        setRadiusResult = new MutableLiveData<>();

        radiusLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<Boolean> getSetRadiusResult() {
        return setRadiusResult;
    }

    public MutableLiveData<String> getRadiusLiveData() { return radiusLiveData; }

    public void setRadius(String radius) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("setting", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("radius", radius);
        editor.commit();
        setRadiusResult.setValue(true);

    }

    public void getRadius() {
        SharedPreferences sharedPreferences = application.getSharedPreferences("setting", Context.MODE_PRIVATE);

        String radius = sharedPreferences.getString("radius" , "0.5");
        getRadiusLiveData().setValue(radius);
    }
}
