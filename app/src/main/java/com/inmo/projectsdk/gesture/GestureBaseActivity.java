package com.inmo.projectsdk.gesture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

public class GestureBaseActivity extends AppCompatActivity {
    private static final String TAG = GestureBaseActivity.class.getSimpleName();
    private static final float FLIP_DISTANCE = 130;

    private GestureDetector mDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startListenGesture();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListenGesture();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector != null) {
            mDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


    protected void startListenGesture() {
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            //单击
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.i(TAG, "onSingleTapConfirmed");
                if (listener != null) {
                    listener.onSingleClick();
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (listener != null) {
                    listener.onLongPress();
                }
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                Log.d(TAG, "onFling velocityX =" + velocityX);
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                Log.d(TAG, "onDoubleTap");
                if (listener != null) {
                    listener.onDoubleClick();
                }
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1.getX() - e2.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 0.5
                        && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 2 && e2.getY() - e1.getY() > 0) {
                    Log.i(TAG, "左下滑动");
                    if (listener != null) {
                        listener.onLeftGesture();
                    }
                    return true;
                }
                if (e1.getX() - e2.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 0.5
                        && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 2 && e2.getY() - e1.getY() < 0) {
                    Log.i(TAG, "左上滑动");
                    if (listener != null) {
                        listener.onLeftGesture();
                    }
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 0.5
                        && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 2 && e2.getY() - e1.getY() > 0) {
                    Log.i(TAG, "右下滑动");
                    if (listener != null) {
                        listener.onRightGesture();
                    }
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 0.5
                        && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 2 && e2.getY() - e1.getY() < 0) {
                    Log.i(TAG, "右上滑动");
                    if (listener != null) {
                        listener.onRightGesture();
                    }
                    return true;
                }
                if (e1.getX() - e2.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 2) {
                    Log.i(TAG, "向左滑动");
                    if (listener != null) {
                        listener.onLeftGesture();
                    }
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) > 2) {
                    Log.i(TAG, "向右滑动");
                    if (listener != null) {
                        listener.onRightGesture();
                    }
                    return true;
                }
                if (e1.getY() - e2.getY() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 0.5) {
                    Log.i(TAG, "向上滑动");
                    return true;
                }
                if (e2.getY() - e1.getY() > FLIP_DISTANCE && Math.abs(e1.getX() - e2.getX()) / Math.abs(e1.getY() - e2.getY()) < 0.5) {
                    Log.i(TAG, "向下滑动");
                    return true;
                }
                return false;
            }
        });
    }

    private void stopListenGesture() {
        if (mDetector != null) {
            mDetector.setContextClickListener(null);
        }
    }

    private onGestureEvent listener;

    protected void setOnGestureEvent(onGestureEvent listener) {
        this.listener = listener;
    }

    public interface onGestureEvent {
        void onLeftGesture();

        void onRightGesture();

        void onSingleClick();

        void onDoubleClick();

        void onLongPress();

    }
}

