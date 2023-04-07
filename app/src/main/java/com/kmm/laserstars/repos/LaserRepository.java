package com.kmm.laserstars.repos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.models.UserType;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.Callback_Impl;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaserRepository {
    private static LaserRepository instance;
    private static API api;
    private final String TAG = LaserRepository.class.getName();

    public static LaserRepository getInstance() {
        if (instance == null)
            instance = new LaserRepository();
        return instance;
    }

    private LaserRepository() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api = retrofit.create(API.class);
    }

    public MutableLiveData<GetData<ArrayList<Design>>> getAllDesigns() {

        MutableLiveData<GetData<ArrayList<Design>>> liveData = new MutableLiveData<>();

        api.getAllDesigns().enqueue(new Callback_Impl<>(liveData, TAG, "getAllDesigns"));
        return liveData;
    }

    public MutableLiveData<GetData<ArrayList<TagGenre>>> getAllTagGenres() {
        MutableLiveData<GetData<ArrayList<TagGenre>>> liveData = new MutableLiveData<>();
        api.getAllTagGenres().enqueue(new Callback_Impl<>(liveData, TAG, "getAllTags"));
        return liveData;
    }

    public MutableLiveData<GetData<ArrayList<Design>>> getMyDesigns(String token) {

        MutableLiveData<GetData<ArrayList<Design>>> liveData = new MutableLiveData<>();
        api.getMyDesigns("Bearer " + token)
                .enqueue(new Callback_Impl<>(liveData, TAG, "getMyDesigns"));
        return liveData;
    }

    public MutableLiveData<GetData<User>> login(String username, String password) {
        MutableLiveData<GetData<User>> liveData = new MutableLiveData<>();
        api.login(username, password).enqueue(new Callback_Impl<>(liveData, TAG,
                "login"));
        return liveData;
    }


    public LiveData<GetData<String>> createDesign(String token, String designName,
                                                  String designDesc, String designImagePath,
                                                  int[] ids,
                                                  String videoPath, boolean isVideoChanged) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        RequestBody nameRequest = RequestBody.create(MediaType.parse("plan/text"), designName);
        if (designDesc == null)
            designDesc = "";
        RequestBody descRequest = RequestBody.create(MediaType.parse("plan/text"), designDesc);
        File file = new File(designImagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part
                .createFormData("image", file.getName(), image);
        MultipartBody.Part videoMultipartBody = null;
        if (videoPath != null && isVideoChanged) {
            File videFile = new File(videoPath);
            RequestBody video = RequestBody.create(MediaType.parse("video/*"), videFile);
            videoMultipartBody = MultipartBody.Part
                    .createFormData("video", videFile.getName(), video);
        }
        RequestBody[] idBodies = new RequestBody[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idBodies[i] = RequestBody.create(MediaType.parse("plan/text"),
                    String.valueOf(ids[i])); // ids[i]+""  || String.valueOf(id);
        }
        api.createDesign("Bearer " + token, nameRequest, descRequest, multipartBody, idBodies,
                videoMultipartBody)
                .enqueue(new Callback_Impl<>(liveData, TAG,
                        "createDesign"));
        return liveData;
    }

    public LiveData<GetData<String>> updateDesgin(String token, String designName,
                                                  String designDesc,
                                                  String designImagePath, int[] ids, int desginId,
                                                  String videoPath, boolean isVideoChanged) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        RequestBody idRequest = RequestBody.create(MediaType.parse("plan/text"),
                String.valueOf(desginId));
        RequestBody nameRequest = RequestBody.create(MediaType.parse("plan/text"), designName);
        RequestBody descRequest = RequestBody.create(MediaType.parse("plan/text"), designDesc);
        MultipartBody.Part multipartBody = null, videoMultipartBody = null;
        if (designImagePath != null) {
            File file = new File(designImagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody = MultipartBody.Part
                    .createFormData("image", file.getName(), image);
        }
        if (videoPath != null && isVideoChanged) {
            File file = new File(videoPath);
            RequestBody video = RequestBody.create(MediaType.parse("video/*"), file);
            videoMultipartBody = MultipartBody.Part
                    .createFormData("video", file.getName(), video);
        }


        RequestBody[] idBodies = new RequestBody[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idBodies[i] = RequestBody.create(MediaType.parse("plan/text"),
                    String.valueOf(ids[i])); // ids[i]+""  || String.valueOf(id);
        }
        api.updateDesign("Bearer " + token, nameRequest, descRequest, multipartBody,
                idBodies, idRequest,
                videoMultipartBody)
                .enqueue(new Callback_Impl<>(liveData, TAG,
                        "updateDesgin"));
        return liveData;
    }

    public MutableLiveData<GetData<String>> deleteDesign(String token, int id) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        api.deleteDesign("Bearer " + token, id)
                .enqueue(new Callback_Impl<>(liveData, TAG,
                        "deleteDesign"));
        return liveData;
    }

    public MutableLiveData<GetData<User>> me(String token) {
        MutableLiveData<GetData<User>> liveData = new MutableLiveData<>();
        api.me("Bearer " + token)
                .enqueue(new Callback_Impl<>(liveData, TAG, "me"));
        return liveData;
    }

    public MutableLiveData<GetData<ArrayList<User>>> getAllDistributors() {
        MutableLiveData<GetData<ArrayList<User>>> liveData = new MutableLiveData<>();
        api.getAllDistributors()
                .enqueue(new Callback_Impl<>(liveData, TAG, "me"));

        return liveData;
    }

    public MutableLiveData<GetData<User>> createDistributor(String token,
                                                            String name,
                                                            String password) {

        MutableLiveData<GetData<User>> liveData = new MutableLiveData<>();
        api.createDistributor("Bearer " + token, name, password)
                .enqueue(new Callback_Impl<>(liveData, TAG, "createDistributor"));

        return liveData;
    }

    public MutableLiveData<GetData<String>> deleteDistributor(String token, int id) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        api.deleteDistributor("Bearer " + token, id)
                .enqueue(new Callback_Impl<>(liveData, TAG, "deleteDistributor"));

        return liveData;
    }

    public MutableLiveData<GetData<String>> removeTag(String token, int tagId) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        api.deleteTag("Bearer " + token, tagId)
                .enqueue(new Callback_Impl<>(liveData, TAG, "removeTag"));

        return liveData;
    }

    public LiveData<GetData<Tag>> addTag(String token, String tagName, int genreId) {
        MutableLiveData<GetData<Tag>> liveData = new MutableLiveData<>();
        api.addTag("Bearer " + token, tagName, genreId)
                .enqueue(new Callback_Impl<>(liveData, TAG, "addTag"));

        return liveData;
    }

    public MutableLiveData<GetData<ArrayList<Design>>> getDesignsByTags(int[] ids) {
        MutableLiveData<GetData<ArrayList<Design>>> liveData = new MutableLiveData<>();
        RequestBody[] idBodies = new RequestBody[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idBodies[i] = RequestBody.create(MediaType.parse("plan/text"),
                    String.valueOf(ids[i])); // ids[i]+""  || String.valueOf(id);
        }
        api.getDesignsByTags(idBodies)
                .enqueue(new Callback_Impl<>(liveData, TAG, "getDesignsByTags"));
        return liveData;
    }

    public MutableLiveData<GetData<String>> updateDistributor(String token,
                                                              int id,
                                                              String name,
                                                              String phone, File file) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        RequestBody idRequest = RequestBody.create(MediaType.parse("plan/text"), String.valueOf(id));
        RequestBody nameRequest = RequestBody.create(MediaType.parse("plan/text"), name);
        RequestBody phoneRequest = RequestBody.create(MediaType.parse("plan/text"), phone);
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part
                .createFormData("avatar_url", file.getName(), image);
        api.updateDistributor("Bearer " + token,
                idRequest, nameRequest, phoneRequest, multipartBody)
                .enqueue(new Callback_Impl<>(liveData, TAG, "updateDistributor"));
        return liveData;
    }

    public MutableLiveData<GetData<ArrayList<Tag>>> getAllTags() {
        MutableLiveData<GetData<ArrayList<Tag>>> liveData = new MutableLiveData<>();
        api.getAllTags().enqueue(new Callback_Impl<>(liveData, TAG, "getAllTags"));
        return liveData;
    }

    public MutableLiveData<GetData<TagGenre>> addNewTagGenre(String genre, String token) {
        MutableLiveData<GetData<TagGenre>> liveData = new MutableLiveData<>();
        api.addTagGenre("Bearer " + token, genre)
                .enqueue(new Callback_Impl<>(liveData, TAG, "addNewTagGenre"));
        return liveData;
    }

    public MutableLiveData<GetData<String>> removeGenre(String token, int id) {
        MutableLiveData<GetData<String>> liveData = new MutableLiveData<>();
        api.removeGenre("Bearer " + token, id)
                .enqueue(new Callback_Impl<>(liveData, TAG, "removeGenre"));
        return liveData;
    }
}
