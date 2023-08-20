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
import com.inmo.projectsdk.utils.ApiService;
import com.inmo.projectsdk.utils.Check_internet_connection;
import com.inmo.projectsdk.utils.RetrofitClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Audio extends AppCompatActivity {


    private ApiService apiService;

    private static final int MICROPHONE_PERMISSION_CODE = 200;
    final String TAG = "MainAc";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    //RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_recorder);
        progressBar = findViewById(R.id.progressBar_cyclic);
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }
        Log.d(TAG, "onCreate: " + getRecordingFilePath());
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

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


    public void apiCalling(View view) {
        if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {
            okhttpRequest();
        } else {
            showToast(getString(R.string.check_internet_connection));
        }

    }

    private void okhttpRequest() {
        String apiKey = "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";
        String model = "whisper-1";

        File audioFile = getRecordingFile();

        RequestBody modelBody = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/mp3"), audioFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", audioFile.getName(), fileBody);

        Call<ResponseBody> call = apiService.uploadAudio("Bearer " + apiKey, modelBody, filePart);
        showLoader();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoader();
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        // Parse the JSON response
                        String text = responseBody; // Update with the actual parsing logic
                        showToast("Success: " + text);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast( "Error parsing response");
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoader();
                showToast("Network Error: " + t.getMessage());
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void btnStopPressed(View view) {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        showToast("Recording is stopped");
    }

    public void btnPlayPressed(View view) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

           showToast("Recording is playing");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleErrorResponse(Response<ResponseBody> response) {
        try {
            String responseBody = response.errorBody().string();
            JSONObject errorJson = new JSONObject(responseBody);

            if (errorJson.has("error")) {
                JSONObject errorObject = errorJson.getJSONObject("error");
                String errorMessage = errorObject.optString("message");
                String errorCode = errorObject.optString("code");

                // Now you can use errorMessage and errorCode to display appropriate feedback to the user
                showToast("Error: " + errorCode);
            } else {
                showToast("Unknown Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error parsing error response");
        }
    }

    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
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