package com.kmm.laserstars.ui.my_desgines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class MyDesignViewModel extends ViewModel {
    private final LaserRepository laserRepository;

    public MyDesignViewModel() {
        laserRepository = LaserRepository.getInstance();
    }

    LiveData<GetData<ArrayList<Design>>> getMyDesigns(String token){

        return laserRepository.getMyDesigns(token);
    }
}