/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exina.android.calendar;

import com.flapps.mobile.android.flapps.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class Cell {
	public static int EMPTY = 0; 
	public static int ACTIVE = 1; 
	public static int DONE = 2; 

	protected Rect mBound = null;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	protected Paint mPaintLine = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);

	int dx, dy;
	protected Drawable mCornerIcon;
	private int mState = Cell.EMPTY;
	private Context mContext = null;

	public Cell(Context context, int dayOfMon, Rect rect, float textSize, boolean bold) {
		mContext = context;
		mDayOfMonth = dayOfMon;
		mBound = rect;
		mPaint.setTextSize(textSize/*26f*/);
		mPaint.setColor(Color.BLACK);
		if(bold) mPaint.setFakeBoldText(true);
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;

	}
	
	public Cell(Context context, int dayOfMon, Rect rect, float textSize) {
		this( context, dayOfMon, rect, textSize, false);
	}
	
	protected void draw(Canvas canvas) {
		if (mState > 0)
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		else mPaint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
		mPaintLine.setColor(Color.LTGRAY);
		if (mBound.top > (mBound.bottom - mBound.top)) canvas.drawLine(mBound.left, mBound.top, mBound.right, mBound.top, mPaintLine);
		if (mBound.left != 0) canvas.drawLine(mBound.left, mBound.bottom, mBound.left, mBound.top, mPaintLine);
//		if (mState > 0) {
//			mCornerIcon.setBounds(mBound);
//			mCornerIcon.draw(canvas);
//		}
	}
	
	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}
	
	public void setState( int state ) {
		mState = state;
		switch (mState) {
		case 1: mCornerIcon = mContext.getResources().getDrawable(R.drawable.calendar_active);
		break;
		case 2: mCornerIcon = mContext.getResources().getDrawable(R.drawable.calendar_done);
		break;
		}
		
	}
	
}

