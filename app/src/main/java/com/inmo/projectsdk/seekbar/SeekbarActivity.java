package com.inmo.projectsdk.seekbar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inmo.projectsdk.R;
import com.inmo.projectsdk.utils.ToastUtils;

public class SeekbarActivity extends AppCompatActivity {
    SeekBar seekBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);
        seekBar = findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                ToastUtils.showToastMessage(SeekbarActivity.this, "progress: " + progress);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        seekBar.onTouchEvent(ev);
        return true;

//        return super.dispatchTouchEvent(ev);
    }
}
