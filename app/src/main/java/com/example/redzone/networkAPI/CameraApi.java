package com.example.redzone.networkAPI;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface CameraApi {

    String DJANGO_SITE="http://13.125.171.174:8000/";

    @Multipart
    @POST("/android/image/")
    //Call<RequestBody> uploadImage(@Part MultipartBody.Part file, @Part MultipartBody.Part id, @Part MultipartBody.Part latitude, @Part MultipartBody.Part longitude);
    Call<RequestBody> uploadImage(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params);
}