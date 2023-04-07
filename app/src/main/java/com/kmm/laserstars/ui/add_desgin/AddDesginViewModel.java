package com.kmm.laserstars.ui.add_desgin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.repos.LaserRepository;

import java.util.ArrayList;

public class AddDesginViewModel extends ViewModel {

    private final LaserRepository laserRepository;
    private final MutableLiveData<Boolean> uploadModel, uploadVideoModel;

    public AddDesginViewModel() {
        laserRepository = LaserRepository.getInstance();
        uploadModel = new MutableLiveData<>(false);
        uploadVideoModel = new MutableLiveData<>(false);
    }

    LiveData<GetData<ArrayList<TagGenre>>> getAllTagGenres() {

        return laserRepository.getAllTagGenres();
    }

    public MutableLiveData<Boolean> getUploadModel() {
        return uploadModel;
    }

    public LiveData<GetData<String>> createDesign(String token, String designName, String designDesc,
                                                  String designImagePath, int[] ids,
                                                  String videoPath, boolean isVideoChanged) {
        return laserRepository.createDesign(token, designName, designDesc, designImagePath, ids,
                videoPath,isVideoChanged);
    }

    public LiveData<GetData<String>> updateDesign(String token, String designName, String designDesc,
                                                  String designImagePath, int[] ids, int desginId,
                                                  String videoPath, boolean isVideoChanged) {
        return laserRepository.updateDesgin(token, designName, designDesc
                , designImagePath, ids, desginId,videoPath,isVideoChanged);
    }

    public LiveData<GetData<String>> deleteDesign(String token, int id) {
        return laserRepository.deleteDesign(token, id);
    }

    public MutableLiveData<Boolean> getUploadVideoModel() {
        return uploadVideoModel;
    }
}