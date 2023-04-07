package com.kmm.laserstars.activities.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class SplashViewModel extends ViewModel {
    private final LaserRepository repository;

    public SplashViewModel() {
        repository = LaserRepository.getInstance();
    }

    LiveData<GetData<ArrayList<Design>>> getAllDesigns() {
        return repository.getAllDesigns();
    }

}
