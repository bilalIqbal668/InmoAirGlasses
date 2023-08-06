package com.inmo.projectsdk.inmoring;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inmo.projectsdk.R;
import com.inmo.projectsdk.utils.ToastUtils;

public class InmoRingActivity extends AppCompatActivity {
    private static final String TAG = "InmoRingActivity";
    private static final int KEY_SHORTCLICK1 = 290;
    private static final int KEY_SHORTCLICK2 = 291;
    private static final int KEY_LONGPRESS1 = 292;
    private static final int KEY_EXIT = KeyEvent.KEYCODE_BACK;

    private TextView connectedState;
    private ImageView state;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmoring);

        connectedState = findViewById(R.id.tv);
        state = findViewById(R.id.state);

        boolean isConnected = isBluetoothHidConnected();
        if(isConnected) {
            state.setVisibility(View.VISIBLE);
            state.setImageLevel(0);
        } else {
            connectedState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InmoRingActivity.this.finish();
            }
        }, 1500);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp keycode: " + keyCode);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                state.setImageLevel(0);
            }
        }, 250);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown keycode: " + keyCode);
        switch(keyCode) {
            case KEY_SHORTCLICK1:
                state.setImageLevel(1);
                break;
            case KEY_SHORTCLICK2:
                state.setImageLevel(2);
                break;
            case KEY_LONGPRESS1:
                state.setImageLevel(4);
                break;
            case KEY_EXIT:
                state.setImageLevel(3);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isBluetoothHidConnected() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter.isEnabled()) {
            int isConnected = adapter.getProfileConnectionState(4);

            if(isConnected == BluetoothProfile.STATE_CONNECTED) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
