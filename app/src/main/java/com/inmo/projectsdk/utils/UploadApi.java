package com.inmo.projectsdk.utils;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadApi {
    @Multipart
    @POST(ServerURL.server_url)
    Call<ResponseData> uploadDriver(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> fileList);


}
