package com.macrovideo.animate;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.macrovideo.sdk.tools.Functions;

import com.macrovideo.demo.R;

public class RadarView extends BaseView {
    public static final String TAG = "SearchDevicesView";
    public static final boolean D = false;

    @SuppressWarnings("unused")
    private long TIME_DIFF = 2000;

    private float offsetArgs = 0;
    private boolean isSearching = false;
    private Bitmap bitmap;
    private Bitmap bitmap2;

    public boolean isSearching() {
        return isSearching;
    }

    public void startAnimate() {
        this.isSearching = true;
        offsetArgs = 0;
        invalidate();
    }

    public void stopAnimate() {
        this.isSearching = false;
        offsetArgs = 0;
        invalidate();
    }

    public RadarView(Context context) {
        super(context);
        initBitmap();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBitmap();
    }

    private void initBitmap() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(Functions.readBitMap(context, R.drawable.radar_area));
        }

        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.radar_fan));
        }
    }

    public void recycleBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect dst = new Rect(0, (getHeight() - getWidth()) / 2, getWidth(), (getHeight() + getWidth()) / 2);
        canvas.drawBitmap(bitmap, null, dst, null);

        if (isSearching) {
            canvas.rotate(offsetArgs, getWidth() / 2, getHeight() / 2);
            canvas.drawBitmap(bitmap2, null, dst, null);
            offsetArgs = offsetArgs + 3;
        } else {
            canvas.drawBitmap(bitmap2, getWidth() / 2 - bitmap2.getWidth(), getHeight() / 2, null);
        }

        if (isSearching) {
            invalidate();
        }
    }
}