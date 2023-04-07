package com.kmm.laserstars.ui.show_all_distributors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class ShowAllDistributorsViewModel extends ViewModel {
    private final LaserRepository laserRepository = LaserRepository.getInstance();

    LiveData<GetData<ArrayList<User>>>getAllDistributors(){
        return laserRepository.getAllDistributors();
    }

    public LiveData<GetData<String>> deleteDis(String token, int id) {
        return laserRepository.deleteDistributor(token,id);
    }
}