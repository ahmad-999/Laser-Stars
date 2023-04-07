package com.kmm.laserstars.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.repos.LaserRepository;

import java.io.File;

public class AccountViewModel extends ViewModel {
    private LaserRepository repository = LaserRepository.getInstance();
    public LiveData<GetData<User>> me(String token) {
       return repository.me(token);
    }
    public LiveData<GetData<String>> updateUser(String token,
                                                int id,
                                                String name,
                                                String phone,
                                                File image){
        return repository.updateDistributor(token,id,name,phone,image);
    }
}