package com.inmo.projectsdk.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

   // @Headers("OpenAI-Organization: org-kdWkjWdiROV1831kXByGMusV")
    @Multipart
    @POST("v1/audio/transcriptions")
    Call<ResponseBody> uploadAudio(
            @Header("Authorization") String authorization,
          //  @Header("OpenAI-Organization") String organization,
            @Part("model") RequestBody model,
            @Part MultipartBody.Part file
    );
}
