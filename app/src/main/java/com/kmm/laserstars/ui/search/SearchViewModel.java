package com.kmm.laserstars.ui.search;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private final LaserRepository laserRepository;
    private final MutableLiveData<GetData<ArrayList<TagGenre>>> genreLiveData;

    public SearchViewModel() {
        laserRepository = LaserRepository.getInstance();
        this.genreLiveData = new MutableLiveData<>();
    }

    LiveData<GetData<ArrayList<Design>>> getDesignsByTags(int[]ids){
        return laserRepository.getDesignsByTags(ids);
    }

    void refreshTagGenres(LifecycleOwner owner) {
        laserRepository.getAllTagGenres()
                .observe(owner, this.genreLiveData::postValue);
    }

    LiveData<GetData<ArrayList<TagGenre>>> getAllTagGenres() {
        return this.genreLiveData;
    }
}