/*
 * MIT License
 *
 * Copyright (c) 2021 Tianscar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.tianscar.colorview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ColorView extends View {

	private final Paint mBgPaint;
	private final Paint mColorPaint;
	private int mBgColor1;
	private int mBgColor2;
	private int mBgPixelSize;
	private float mCornerRadius;

	public ColorView(@NonNull Context context){
		this(context, null);
	}
	public ColorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ColorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		mBgPaint = new Paint();
		mBgPaint.setStyle(Paint.Style.FILL);
		mBgPaint.setAntiAlias(true);
		mBgPaint.setDither(true);
		mColorPaint = new Paint();
		mColorPaint.setStyle(Paint.Style.FILL);
		mColorPaint.setAntiAlias(true);
		mColorPaint.setDither(true);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorView,
				defStyleAttr, 0);
		mBgColor1 = typedArray.getInt(R.styleable.ColorView_backgroundColor1, Color.LTGRAY);
		mBgColor2 = typedArray.getInt(R.styleable.ColorView_backgroundColor2, Color.GRAY);
		mCornerRadius = typedArray.getDimension(R.styleable.ColorView_cornerRadius,
				getResources().getDimension(R.dimen.colorview_default_radius));
		mBgPixelSize = typedArray.getDimensionPixelSize(R.styleable.ColorView_backgroundPixelSize,
				getResources().getDimensionPixelSize(R.dimen.colorview_default_pixel_size));
		mColorPaint.setColor(typedArray.getColor(R.styleable.ColorView_color, Color.TRANSPARENT));
		typedArray.recycle();
		setClickable(true);
    }

	public float getCornerRadius() {
		return mCornerRadius;
	}

	public void setCornerRadius(float radius) {
		this.mCornerRadius = radius;
	}

	public void setBackgroundColor1(int color) {
		mBgColor1 = color;
		invalidate();
	}

	public void setBackgroundColor2(int color) {
		mBgColor2 = color;
		invalidate();
	}

	public void setBackgroundColors(int color1, int color2) {
		mBgColor1 = color1;
		mBgColor2 = color2;
		invalidate();
	}

	public int getBackgroundColor1() {
		return mBgColor1;
	}

	public int getBackgroundColor2() {
		return mBgColor2;
	}

	public int getBackgroundPixelSize() {
		return mBgPixelSize;
	}

	public void setBackgroundPixelSize(int pixelSize) {
		mBgPixelSize = pixelSize;
	}

	public void setColor(int color) {
		mColorPaint.setColor(color);
		invalidate();
	}
	
	public int getColor() {
		return mColorPaint.getColor();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
		}
		else if(widthSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(getMeasuredWidth(), heightSpecSize);
		}
		else if(heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(widthSpecSize, getMeasuredHeight());
		}
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bgBitmap = Bitmap.createBitmap(new int[] {
				mBgColor1, mBgColor2, mBgColor2, mBgColor1
		}, 2, 2, Bitmap.Config.ARGB_8888);
		Bitmap shaderBitmap = Bitmap.createScaledBitmap(bgBitmap, 2 * mBgPixelSize,
				2 * mBgPixelSize, false);
		bgBitmap.recycle();
		mBgPaint.setShader(new BitmapShader(shaderBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
		canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
				mCornerRadius, mCornerRadius, mBgPaint);
		shaderBitmap.recycle();

		canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
				mCornerRadius, mCornerRadius, mColorPaint);

	}

}