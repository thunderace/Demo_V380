package com.macrovideo.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundCornerImageView extends ImageView {

	public RoundCornerImageView(Context context) {
		super(context);
		// add 2016��9��23��
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// API
			setLayerType(LAYER_TYPE_SOFTWARE, null);// ����Ӳ������
		}
	}

	public RoundCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// add 2016��9��23��
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// API
			setLayerType(LAYER_TYPE_SOFTWARE, null);// ����Ӳ������
		}
	}

	public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// add 2016��9��23��
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// API
			setLayerType(LAYER_TYPE_SOFTWARE, null);// ����Ӳ������
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f, Path.Direction.CW);
		canvas.clipPath(clipPath);
	}
}