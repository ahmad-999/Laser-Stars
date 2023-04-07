package com.kmm.laserstars.ui.add_distributor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.repos.LaserRepository;

public class AddDistributorViewModel extends ViewModel {

    private final LaserRepository repository = LaserRepository.getInstance();

    public LiveData<GetData<User>> createDistributor(String token,String name,String password){
        return repository.createDistributor(token,name,password);
    }
}