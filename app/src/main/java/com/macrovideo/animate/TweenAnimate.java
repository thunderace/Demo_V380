package com.macrovideo.animate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.macrovideo.sdk.tools.Functions;
import com.macrovideo.demo.R;

public class TweenAnimate extends BaseView {
    private Animation alphaAnimation = null;

    private Animation scaleAnimation = null;

    private Animation translateAnimation = null;

    private Animation rotateAnimation = null;
    private Bitmap bitmap;

    public TweenAnimate(Context context) {
        super(context);
        initBitmap();
    }

    public TweenAnimate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
    }

    public TweenAnimate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void initBitmap() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(Functions.readBitMap(context, R.drawable.radar_fan));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                alphaAnimation.setDuration(3000);
                this.startAnimation(alphaAnimation);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                rotateAnimation = new RotateAnimation(0f, 360f);
                rotateAnimation.setDuration(1000);
                this.startAnimation(rotateAnimation);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                scaleAnimation.setDuration(500);
                this.startAnimation(scaleAnimation);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                translateAnimation = new TranslateAnimation(0.1f, 100.0f, 0.1f, 100.0f);
                translateAnimation.setDuration(1000);

                this.startAnimation(translateAnimation);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                translateAnimation = new TranslateAnimation(0.1f, 100.0f, 0.1f, 100.0f);
                alphaAnimation = new AlphaAnimation(0.1f, 1.0f);

                AnimationSet set = new AnimationSet(true);
                set.addAnimation(translateAnimation);
                set.addAnimation(alphaAnimation);

                set.setDuration(1000);
                this.startAnimation(set);
                break;
            default:
                break;
        }
        return true;
    }
}