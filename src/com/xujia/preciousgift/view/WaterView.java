package com.xujia.preciousgift.view;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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

public class WaterView extends View implements View.OnTouchListener{
	private Bitmap mBitmap;
	long time;
	long fps;
	public WaterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.background3);
	        mBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
	        AnimRender.setBitmap(bmp);
	        this.setOnTouchListener(this);
	}

	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
		  int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	      if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)	{
	    	  setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());  
	      }else	{
	    	  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      }


	}

	 @Override 
	    protected void onDraw(Canvas canvas) {
	    	
		 long ct = System.currentTimeMillis();
	    	if(ct - time > 1000){
	    	
	    		time = ct;
	    		fps = 0;
	    	}
	    	//fps++;
	    	fps += 20;
	    	
	        AnimRender.render(mBitmap);    	
	        canvas.drawBitmap(mBitmap, 0, 0, null);
	        postInvalidate();
	    }
	 
	 @Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			AnimRender.drop((int)event.getX(), (int)event.getY(), 1200);
			return false;
		}

}
class AnimRender{
	public static native void setBitmap(Bitmap src);
    public static native void render(Bitmap dst);
    public static native void drop(int x, int y, int height);
   
    static {
        System.loadLibrary("plasma");
    }
}
