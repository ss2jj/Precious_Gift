package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViewTwo extends View {
	private Canvas mCanvas = null;
	private Path mPath = null;
	private Paint mPaint = null;
	private Bitmap bitmap = null;
	private int screenWidth = 200;
	private int screenHeight = 200;
	private static final String TAG = "ViewTwo";
	public ViewTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		init(context);
	}

	public void init(Context context)	{
		mPath = new Path();
		bitmap =  Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
		//bitmap.setPixel(100, 100,Color.GRAY);
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeJoin(Join.ROUND);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		mPaint.setAlpha(0);

		mCanvas = new Canvas(bitmap);
		mCanvas.drawColor(Color.GRAY);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
		  int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	      if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)	{
	    	  setMeasuredDimension(screenWidth, screenHeight);  
	      }else	{
	    	  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      }


	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自动生成的方法存根
		super.onDraw(canvas);
		mCanvas.drawPath(mPath, mPaint);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	int x = 0;
	int y = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int currX = (int) event.getX();
		int currY = (int) event.getY();
		Log.d(TAG, "currX "+currX+ "currY "+currY);
		switch(action){
		case MotionEvent.ACTION_DOWN:{
			mPath.reset();
			x = currX;
			y = currY;
			Log.d(TAG, "startpoint x  "+x+ "y "+y);
			mPath.moveTo(x, y);
		}break;
		case MotionEvent.ACTION_MOVE:{
			Log.d(TAG, "endpoint currX  "+currX+ "currY "+currY);
			mPath.quadTo(x, y, currX, currY);
			x = currX;
			y = currY;
			postInvalidate();
		}break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:{
			mPath.reset();
		}break;
		}
		return true;
	}

}
