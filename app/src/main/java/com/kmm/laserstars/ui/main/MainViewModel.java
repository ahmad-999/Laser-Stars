package com.kmm.laserstars.ui.main;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final LaserRepository laserRepository;
    private final MutableLiveData<GetData<ArrayList<TagGenre>>> genreLiveData;

    public MainViewModel() {
        laserRepository = LaserRepository.getInstance();
        this.genreLiveData = new MutableLiveData<>();

    }

    LiveData<GetData<ArrayList<Design>>> getAllDesigns() {

        return laserRepository.getAllDesigns();
    }

    void refreshTagGenres(LifecycleOwner owner) {
        laserRepository.getAllTagGenres()
                .observe(owner, this.genreLiveData::postValue);
    }

    LiveData<GetData<ArrayList<TagGenre>>> getAllTagGenres() {
        return this.genreLiveData;
    }
}