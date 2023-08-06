package com.inmo.projectsdk.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration{
    private int mPageMargin = 30;
    private int mLeftPageVisibleWidth;
    private int mRightPageVisibleWidth;

    public void setmPageMargin(int mPageMargin) {
        this.mPageMargin = mPageMargin;
    }

    public void setmLeftPageVisibleWidth(int mLeftPageVisibleWidth) {
        this.mLeftPageVisibleWidth = mLeftPageVisibleWidth;
    }

    public void setmRightPageVisibleWidth(int mRightPageVisibleWidth) {
        this.mRightPageVisibleWidth = mRightPageVisibleWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mLeftPageVisibleWidth == 0) {
            mLeftPageVisibleWidth = 320;

        }
        if(mRightPageVisibleWidth == 0) {
            mRightPageVisibleWidth = 320;
        }

        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        int leftMagin;
        if (position == 0){
            leftMagin= dpToPx(mLeftPageVisibleWidth);
        }else{
            leftMagin=dpToPx(mPageMargin);
        }
        int rightMagin;
        if (position == itemCount-1) {
            rightMagin=dpToPx(mRightPageVisibleWidth);
        }else{
            rightMagin=dpToPx(mPageMargin);
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();

        layoutParams.setMargins(leftMagin,0,rightMagin,0);
        view.setLayoutParams(layoutParams);

        super.getItemOffsets(outRect, view, parent, state);


    }

    /**
     * d p转换成px
     * @param dp：
     */
    private int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);

    }

    /**
     * 获取屏幕的宽度
     * @param context:
     * @return :
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
}
