package com.inmo.projectsdk.gesture;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.inmo.projectsdk.R;

public class GestureActivity extends GestureBaseActivity implements GestureBaseActivity.onGestureEvent{
    private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        setOnGestureEvent(this);
        tv = findViewById(R.id.tv);

        tv.setText("Please touch the TouchPad");
    }

    @Override
    public void onLeftGesture() {
        tv.setText("OnSwipeBackward");
    }

    @Override
    public void onRightGesture() {
        tv.setText("OnSwipeForward");
    }

    @Override
    public void onSingleClick() {
        tv.setText("OnSingleTap");
    }

    @Override
    public void onDoubleClick() {
        tv.setText("OnDoubleTap");
    }

    @Override
    public void onLongPress() {
        tv.setText("onLongPress");
    }
}
