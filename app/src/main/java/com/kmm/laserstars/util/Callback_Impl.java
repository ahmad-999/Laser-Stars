package com.kmm.laserstars.util;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Callback_Impl<T> implements Callback<T> {
    private final MutableLiveData<T> liveData;
    private final String TAG;
    private final String methodName;

    public Callback_Impl(MutableLiveData<T> liveData, String tag, String methodName) {
        this.liveData = liveData;
        TAG = tag;
        this.methodName = methodName;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            liveData.postValue(response.body());
        } else {
            liveData.postValue(null);
            Log.e(TAG, methodName + "onResponse | status code : " + response.code());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        liveData.postValue(null);
        Log.e(TAG, methodName + " onFailure : " + t.getMessage());
    }
}
