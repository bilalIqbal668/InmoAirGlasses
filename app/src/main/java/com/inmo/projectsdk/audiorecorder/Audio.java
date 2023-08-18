package com.inmo.projectsdk.audiorecorder;

import static android.os.Environment.DIRECTORY_MUSIC;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.inmo.projectsdk.R;
import com.inmo.projectsdk.utils.PRRequestBody;
import com.inmo.projectsdk.utils.ResponseData;
import com.inmo.projectsdk.utils.UploadApi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Audio extends AppCompatActivity {

    private static final int MICROPHONE_PERMISSION_CODE = 200;
    final String TAG = "MainAc";
    //List<String> fileList = new ArrayList<>(),fileList2 = new ArrayList<>();
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    //RecyclerView recyclerView;
    OkHttpClient client = new OkHttpClient();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/audio/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    UploadApi service = retrofit.create(UploadApi.class);
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_recorder);
progressBar=findViewById(R.id.progressBar_cyclic);
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }
        Log.d(TAG, "onCreate: " + getRecordingFilePath());
    }


    public void btnRecordPressed(View view) {
        Log.d(TAG, "btnRecordPressed: " + getRecordingFilePath());
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Recording is started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void apiCalling(View view)  {

/*
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),getRecordingFile());

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("", getRecordingFile().getName(), fileBody);

        RequestBody name = MultipartBody.create(MediaType.parse("multipart/form-data"), "whisper-1");
        Call<ResponseBody>
        call = service.uploadDriver("Bearer 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",filePart,name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response.code()+"message"+response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());

            }
        });*/

      /*  ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    okhttpRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    executor.shutdown();
                }
            }
        });*/
        progressBar.setVisibility(View.VISIBLE);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                3000
        );

      //  progressBar.setVisibility(View.GONE);

    }

    private void okhttpRequest() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file",getRecordingFile().getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), getRecordingFile()))
                .addFormDataPart("model","whisper-1")
                .build();
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/audio/transcriptions")
                                .method("POST", body)
                                .addHeader("Authorization", "Bearer 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")
                                .build();
        okhttp3.Response response = client.newCall(request).execute();

        Log.d("Response88","Response "+response.body());
        Log.d("Response88","Response "+response.message());
    }

    public void btnStopPressed(View view) {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this, "Recording is stopped", Toast.LENGTH_SHORT).show();
    }

    public void btnPlayPressed(View view) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Recording is playing", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath() {

        List<Integer> ints = new ArrayList<>();

        String child = "testRecordingFile";

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File Directory = contextWrapper.getExternalFilesDir(DIRECTORY_MUSIC);

        File file = new File(Directory, child + (getMax(ints) + 1) + ".mp3");
        Log.d(TAG, "getRecordingFilePath: " + file.getPath() + " " + getMax(ints));

        return file.getPath();
    }
    private File getRecordingFile() {

        List<Integer> ints = new ArrayList<>();

        String child = "testRecordingFile";

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File Directory = contextWrapper.getExternalFilesDir(DIRECTORY_MUSIC);

        File file = new File(Directory, child + (getMax(ints) + 1) + ".mp3");
        Log.d(TAG, "getRecordingFilePath: file" + file.getPath() + " " + getMax(ints));

       return file;
    }

    public static Integer getMax(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }

        return Collections.max(list);
    }
}