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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.inmo.projectsdk.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Audio extends AppCompatActivity {

    private static final int MICROPHONE_PERMISSION_CODE = 200;
    final String TAG = "MainAc";
    //List<String> fileList = new ArrayList<>(),fileList2 = new ArrayList<>();
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    //RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_recorder);

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

    public static Integer getMax(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }

        return Collections.max(list);
    }
}