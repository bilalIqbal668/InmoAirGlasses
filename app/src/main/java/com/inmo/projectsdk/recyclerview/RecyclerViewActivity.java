package com.inmo.projectsdk.recyclerview;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.inmo.projectsdk.R;
import com.inmo.projectsdk.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private List<String> items = new ArrayList<>();
    private View lastSelectView = null;
    private int selectPosition = 0;
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("InmoSDK Sample");
        initDetector();
        for (int i = 0; i < 100; ++i) {
            items.add("item" + i);
        }

        initRecyclerView();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDetector != null) {
            mDetector.onTouchEvent(ev);
        }
        if (recyclerView != null) {
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

        ToastUtils.showToastMessage(this, items.get(pos));

    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MyItemDecoration itemDecoration = new MyItemDecoration();
        itemDecoration.setmLeftPageVisibleWidth(315);
        itemDecoration.setmRightPageVisibleWidth(315);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);

        MyAdapter adapter = new MyAdapter(this, items);
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
