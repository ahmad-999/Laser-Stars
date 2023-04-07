package com.kmm.laserstars.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.repos.LaserRepository;

public class LoginViewModel extends ViewModel {
    private final LaserRepository repository;

    public LoginViewModel() {
        repository = LaserRepository.getInstance();
    }

    public LiveData<GetData<User>> login(String username, String password){
        return repository.login(username, password);
    }
}