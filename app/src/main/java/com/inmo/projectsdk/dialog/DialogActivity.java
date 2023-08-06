package com.inmo.projectsdk.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.inmo.projectsdk.R;
import com.inmo.projectsdk.gesture.GestureBaseActivity;
import com.inmo.projectsdk.utils.ToastUtils;

public class DialogActivity extends GestureBaseActivity implements GestureBaseActivity.onGestureEvent{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setOnGestureEvent(this);
    }

    private void showDialog() {
        MyDialog dialog = new MyDialog(this);
        dialog.show();
        dialog.setButtonClickListener(new MyDialog.OnButtonClickListener() {
            @Override
            public void onConfirm() {
                ToastUtils.showToastMessage(DialogActivity.this, "onConfirm");
            }

            @Override
            public void onCancel() {
                ToastUtils.showToastMessage(DialogActivity.this, "onCancel");
            }
        });

    }

    @Override
    public void onLeftGesture() {
    }

    @Override
    public void onRightGesture() {
    }

    @Override
    public void onSingleClick() {
        showDialog();
    }

    @Override
    public void onDoubleClick() {

    }

    @Override
    public void onLongPress() {

    }
}
