package com.inmo.projectsdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.inmo.projectsdk.audiorecorder.Audio;
import com.inmo.projectsdk.recyclerview.MyAdapter;
import com.inmo.projectsdk.recyclerview.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private List<String> pages = new ArrayList<>();
    private View lastSelectView = null;
    private CardView cardView;
    private int selectPosition = 0;
    private GestureDetector mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inmo recorder");
        initDetector();
        /*pages.add("Gesture");
        pages.add("Dialog");
        pages.add("Seekbar");
        pages.add("RecyclerView");
        pages.add("InmoRing");*/
        pages.add("Start recording");

        initRecyclerView();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDetector != null) {
            mDetector.onTouchEvent(ev);
        }
        if (recyclerView!=null){
            MotionEvent ev2 = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), ev.getAction(), -(ev.getX()), ev.getY(), ev.getMetaState());
            recyclerView.dispatchTouchEvent(ev2);
        }
        return true;
    }

    private void initDetector() {
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            //单击
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                performClick();
                return false;
            }

        });
    }

    private void performClick() {
        if (lastSelectView == null) {
            return;
        }

        int pos = recyclerView.getChildLayoutPosition(lastSelectView);
        if (pos < 0) {
            return;
        }
        switch (pos) {
            /*
            case 0:
                goToActivity(GestureActivity.class);
                break;
            case 1:
                goToActivity(DialogActivity.class);
                break;
            case 2:
                goToActivity(SeekbarActivity.class);
                break;
            case 3:
                goToActivity(RecyclerViewActivity.class);
                break;
            case 4:
                goToActivity(InmoRingActivity.class);
                break;
            case 5:
                break;*/
            case 0:
                goToActivity(Audio.class);
                break;
            default:
                break;
        }

    }

    protected void goToActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MyItemDecoration itemDecoration = new MyItemDecoration();
        itemDecoration.setmLeftPageVisibleWidth(315);
        itemDecoration.setmRightPageVisibleWidth(315);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);

        MyAdapter adapter = new MyAdapter(this, pages);
        recyclerView.setAdapter(adapter);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerView != null && layoutManager != null) {
                    recyclerView.smoothScrollToPosition(1);
                    changeSelectedItemView(snapHelper.findSnapView(layoutManager));
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    changeSelectedItemView(snapHelper.findSnapView(layoutManager));
                    selectPosition = recyclerView.getChildLayoutPosition(lastSelectView);
                }
            }
        });


    }

    private void changeSelectedItemView(View view) {
        lastSelectView = view;
    }


}