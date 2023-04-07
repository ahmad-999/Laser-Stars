package com.kmm.laserstars.ui.tags;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class TagsViewModel extends ViewModel {


    private final LaserRepository laserRepository;

    public TagsViewModel() {
        laserRepository = LaserRepository.getInstance();
    }

    LiveData<GetData<ArrayList<TagGenre>>> getAllTags() {

        return laserRepository.getAllTagGenres();
    }

    LiveData<GetData<String>> removeTag(String token, int tagId) {
        return laserRepository.removeTag(token, tagId);
    }

    public LiveData<GetData<Tag>> addTag(String token, String tagName, int genreId) {
        return laserRepository.addTag(token, tagName, genreId);
    }

    public LiveData<GetData<TagGenre>> addNewTagGenre(String genre, String token) {
        return laserRepository.addNewTagGenre(genre, token);
    }

    public LiveData<GetData<String>> removeGenre(String token, int id) {
        return laserRepository.removeGenre(token, id);
    }
}