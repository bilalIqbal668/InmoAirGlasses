package com.inmo.projectsdk.utils;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadApi {
    @Multipart
    @POST(ServerURL.server_url)
    Call<ResponseBody> uploadDriver(@Header("Authorization") String token ,
                                    @Part("file")  MultipartBody.Part file,
                                    @Part("model") RequestBody model );


}
