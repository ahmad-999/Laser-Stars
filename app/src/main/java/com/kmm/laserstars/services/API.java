package com.kmm.laserstars.services;


import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.models.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
//    String BASE_URL = "http://192.168.1.9:8000/";
//    String RES_URL = "http://192.168.1.9:8000";
    String BASE_URL = "https://www.laser-stars.com/";
    String RES_URL = "https://www.laser-stars.com";

    @POST("/api/get-all-designs")
    Call<GetData<ArrayList<Design>>> getAllDesigns();

    @POST("/api/get-my-designs")
    Call<GetData<ArrayList<Design>>> getMyDesigns(
            @Header("Authorization") String token
    );


    @POST("/api/login")
    @FormUrlEncoded
    Call<GetData<User>> login(
            @Field("name") String name,
            @Field("password") String password
    );

    @POST("/api/get-all-tags")
    Call<GetData<ArrayList<Tag>>> getAllTags();

    @POST("/api/get-all-genre-tags")
    Call<GetData<ArrayList<TagGenre>>> getAllTagGenres();


    @Multipart
    @POST("/api/create-desgin")
    Call<GetData<String>> createDesign(@Header("Authorization") String token,
                                       @Part("name") RequestBody nameRequest,
                                       @Part("desc") RequestBody descRequest,
                                       @Part MultipartBody.Part multipartBody,
                                       @Part("tags[]") RequestBody[] idBodies,
                                       @Part MultipartBody.Part videoMultipartBody);

    @Multipart
    @POST("/api/update-desgin")
    Call<GetData<String>> updateDesign(@Header("Authorization") String token,
                                       @Part("name") RequestBody nameRequest,
                                       @Part("desc") RequestBody descRequest,
                                       @Part MultipartBody.Part multipartBody,
                                       @Part("tags[]") RequestBody[] idBodies,
                                       @Part("id") RequestBody idRequest,
                                       @Part MultipartBody.Part videoMultipartBody);

    @POST("/api/delete-desgin")
    @FormUrlEncoded
    Call<GetData<String>> deleteDesign(@Header("Authorization") String token,
                                       @Field("id") int id);

    @POST("/api/me")
    Call<GetData<User>> me(@Header("Authorization") String token);

    @POST("/api/get-all-distributors")
    Call<GetData<ArrayList<User>>> getAllDistributors();


    @POST("/api/create-distributor")
    @FormUrlEncoded
    Call<GetData<User>> createDistributor(@Header("Authorization") String token,
                                          @Field("name") String name,
                                          @Field("password") String password);

    @POST("/api/delete-distributor")
    @FormUrlEncoded
    Call<GetData<String>> deleteDistributor(@Header("Authorization") String token,
                                            @Field("id") int id);

    @POST("/api/delete-tag/{id}")
    Call<GetData<String>> deleteTag(@Header("Authorization") String token,
                                    @Path("id") int id);

    @POST("/api/create-tag")
    @FormUrlEncoded
    Call<GetData<Tag>> addTag(@Header("Authorization") String token,
                                 @Field("name") String tagName,
                                 @Field("genreId") int genreId);

    @Multipart
    @POST("/api/get-desgin-by-tags")
    Call<GetData<ArrayList<Design>>> getDesignsByTags(
            @Part("tags[]") RequestBody[] idBodies
    );

    @Multipart
    @POST("/api/update-distributor")
    Call<GetData<String>> updateDistributor(
            @Header("Authorization") String token,
            @Part("id") RequestBody idRequest,
            @Part("name") RequestBody nameRequest,
            @Part("phone") RequestBody phoneRequest,
            @Part MultipartBody.Part multipartBody
    );

    @POST("/api/create-tag-genre")
    @FormUrlEncoded
    Call<GetData<TagGenre>> addTagGenre(@Header("Authorization") String token,
                                        @Field("name") String genre);
    @POST("/api/remove-tag-genre")
    @FormUrlEncoded
    Call<GetData<String>> removeGenre(@Header("Authorization") String token,
                                      @Field("id") int id);
}
